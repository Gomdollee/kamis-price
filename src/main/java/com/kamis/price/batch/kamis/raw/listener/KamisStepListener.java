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

    @Override
    public void beforeStep(StepExecution stepExecution) {
        try {
            String productClsCode = stepExecution.getExecutionContext().getString("productClsCode");
            String categoryCode = stepExecution.getExecutionContext().getString("categoryCode");
            String countryCode = stepExecution.getExecutionContext().getString("countryCode");
            String regday = stepExecution.getJobParameters().getString("regday");

            if (regday == null || regday.isBlank()) {
                regday = LocalDate.now().toString();
            }

            LocalDate regDate = LocalDate.parse(regday);

            // request 생성
            KamisRawRequest request = KamisRawRequest.create(
                    productClsCode,
                    categoryCode,
                    countryCode,
                    regDate,
                    kamisApiService.getCertKey(),
                    kamisApiService.getCertKey()
            );

            requestRepository.save(request);

            // API 호출
            var response = kamisApiService.fetchCategoryPrices(
                    productClsCode,
                    categoryCode,
                    countryCode,
                    regday
            );

            // 응답 이상
            if (regday == null || response.getData() == null) {
                request.complete("NO_RESPONSE", "{}");
                markEmpty(stepExecution);
                return;
            }

            // 데이터 없는 케이스 (data = ["001"])
            if (response.getData().getItem() == null || response.getData().getItem().isEmpty()) {
                request.complete("NO_DATA", "{}");
                markEmpty(stepExecution);
                return;
            }

            // 정상 데이터
            request.complete(response.getData().getErrorCode(), "OK");

            stepExecution.getExecutionContext().putLong("requestId", request.getId());
            stepExecution.getExecutionContext().put("items", response.getData().getItem());
        } catch (Exception e) {
            throw new IllegalStateException("Raw Step 초기화 실패", e);
        }
    }

    private void markEmpty(StepExecution stepExecution) {
        stepExecution.getExecutionContext().putLong("requestId", -1L);
        stepExecution.getExecutionContext().put("items", Collections.emptyList());
    }





}
