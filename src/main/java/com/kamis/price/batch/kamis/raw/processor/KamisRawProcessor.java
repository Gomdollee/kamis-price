package com.kamis.price.batch.kamis.raw.processor;

import com.kamis.price.domain.raw.entity.KamisRawItem;
import com.kamis.price.domain.raw.entity.KamisRawRequest;
import com.kamis.price.domain.raw.repository.KamisRawRequestRepository;
import com.kamis.price.external.kamis.dto.KamisItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@StepScope
@RequiredArgsConstructor
public class KamisRawProcessor implements ItemProcessor<KamisItemDto, KamisRawItem>, StepExecutionListener {

    private final KamisRawRequestRepository requestRepository;

    private KamisRawRequest request;

    @Value("#{stepExecutionContext['productClsCode']}")
    private String productClsCode;

    @Value("#{stepExecutionContext['categoryCode']}")
    private String categoryCode;

    @Value("#{stepExecutionContext['countryCode']}")
    private String countryCode;

    @Value("#{jobParameters['regday']}")
    private String regday;

    /**
     * listener에서 만든 request를 조회해 현재 step에 연결
     */
    @Override
    public void beforeStep(StepExecution stepExecution) {
        Long requestId = stepExecution.getExecutionContext().getLong("requestId");
        // 데이터 없는 partition
        if (requestId == -1L) {
            this.request = null;
            return;
        }
        this.request = requestRepository.findById(requestId).orElseThrow();
    }

    @Override
    public KamisRawItem process(KamisItemDto item) {

        if (request == null) {
            return null;
        }

        return KamisRawItem.from(
                request,
                item,
                categoryCode,
                countryCode,
                LocalDate.parse(regday)
        );
    }
}
