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

    @Value("#{jobParameters['categoryCode']}")
    private String categoryCode;

    @Value("#{jobParameters['countryCode']}")
    private String countryCode;

    @Value("#{jobParameters['regday']}")
    private String regday;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        Long requestId = stepExecution.getExecutionContext().getLong("requestId");
        this.request = requestRepository.findById(requestId).orElseThrow();
    }

    @Override
    public KamisRawItem process(KamisItemDto item) {

        return KamisRawItem.from(
                request,
                item,
                categoryCode,
                countryCode,
                LocalDate.parse(regday)
        );
    }
}
