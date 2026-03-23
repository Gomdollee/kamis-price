package com.kamis.price.batch.kamis.raw.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kamis.price.domain.raw.entity.KamisRawRequest;
import com.kamis.price.domain.raw.repository.KamisRawRequestRepository;
import com.kamis.price.external.kamis.dto.KamisResponseDto;
import com.kamis.price.external.kamis.service.KamisApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class KamisStepListener implements StepExecutionListener {

    private final KamisRawRequestRepository requestRepository;
    private final KamisApiService kamisApiService;

    /**
     * beforeStep = worker step이 시작되기 직전에 1번 실행됨
     *
     * [partition 1개] = [조건 1세트] = [API 호출 1번]
     */
    @Override
    public void beforeStep(StepExecution stepExecution) {
        try {

            // partitioner가 넣어둔 현재 partition의 조건값 읽기
            String productClsCode = stepExecution.getExecutionContext().getString("productClsCode");
            String categoryCode = stepExecution.getExecutionContext().getString("categoryCode");
            String countryCode = stepExecution.getExecutionContext().getString("countryCode");
            String regday = stepExecution.getJobParameters().getString("regday");

            if (regday == null || regday.isBlank()) {
                regday = LocalDate.now().toString();
            }

            LocalDate regDate = LocalDate.parse(regday);

            // 1. 현재 API 요청에 대한 request row 먼저 생성
            KamisRawRequest request = KamisRawRequest.create(
                    productClsCode,
                    categoryCode,
                    countryCode,
                    regDate,
                    kamisApiService.getCertKey(),
                    kamisApiService.getCertKey()
            );

            requestRepository.save(request);

            // 2. 외부 API 호출
            var response = kamisApiService.fetchCategoryPrices(
                    productClsCode,
                    categoryCode,
                    countryCode,
                    regday
            );

            // 3. 응답이 비정상이면 "빈 step"으로 처리
            //    markEmpty를 해두면 reader가 읽을 데이터가 없으므로 조용히 끝남
            if (regday == null || response.getData() == null) {
                request.complete("NO_RESPONSE", "{}");
                markEmpty(stepExecution);
                return;
            }

            // 4. 응답은 왔지만 실제 item 데이터가 없으면 역시 빈 step 처리
            if (response.getData().getItem() == null || response.getData().getItem().isEmpty()) {
                request.complete("NO_DATA", "{}");
                markEmpty(stepExecution);
                return;
            }


            // 5. 정상 데이터인 경우
            //    request row 상태를 완료로 바꾸고
            //    이후 reader/processor가 사용할 값들을 ExecutionContext에 저장
            request.complete(response.getData().getErrorCode(), "OK");

            // requestId는 processor가 request 엔티티를 다시 조회하는 데 사용
            stepExecution.getExecutionContext().putLong("requestId", request.getId());

            // items는 reader가 하나씩 꺼내서 읽을 원본 목록
            stepExecution.getExecutionContext().put("items", response.getData().getItem());
        } catch (Exception e) {
            throw new IllegalStateException("Raw Step 초기화 실패", e);
        }
    }

    /**
     * 데이터가 없는 partition을 표시하는 헬퍼 메서드
     *
     * requestId = -1L
     * - processor가 "이번 partition은 처리할 요청이 없다"고 판단하는 신호
     *
     * items = emptyList
     * - reader가 바로 종료되도록 만듦
     */
    private void markEmpty(StepExecution stepExecution) {
        stepExecution.getExecutionContext().putLong("requestId", -1L);
        stepExecution.getExecutionContext().put("items", Collections.emptyList());
    }





}
