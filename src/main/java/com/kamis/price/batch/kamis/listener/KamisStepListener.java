package com.kamis.price.batch.kamis.listener;

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

@Component
@RequiredArgsConstructor
public class KamisStepListener implements StepExecutionListener {

    private final KamisRawRequestRepository requestRepository;
    private final KamisApiService apiService;
    private final ObjectMapper objectMapper;

    @Override
    public void beforeStep(StepExecution stepExecution) {

        String productClsCode = stepExecution.getJobParameters().getString("productClsCode");
        String categoryCode = stepExecution.getJobParameters().getString("categoryCode");
        String countryCode = stepExecution.getJobParameters().getString("countryCode");
        String regday = stepExecution.getJobParameters().getString("regday");

        LocalDate date = LocalDate.parse(regday);

        // 1.request 생성
        KamisRawRequest request = KamisRawRequest.create(
                productClsCode,
                categoryCode,
                countryCode,
                date,
                apiService.getCertKey(),
                apiService.getCertId()
        );

        requestRepository.save(request);

        // 2.API 호출 + rawJson 저장
        try {
            KamisResponseDto response = apiService.fetchCategoryPrices(
                    productClsCode, categoryCode, countryCode, regday
            );

            String rawJson = objectMapper.writeValueAsString(response);
            String errorCode = response.getData() != null ? response.getData().getErrorCode() : null;

            request.complete(errorCode, rawJson);

            // item 리스트도 같이 저장
            stepExecution.getExecutionContext().put("items", response.getData().getItem());

        } catch (Exception e) {
            request.markFailed(e.getMessage());
            throw new RuntimeException(e);
        }

        // requestId 공유
        stepExecution.getExecutionContext().putLong("requestId", request.getId());
    }


}
