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
@StepScope // step 실행 시점마다 새로 생성되는 스코프. stepExecutionContext / jobParameters 값을 안전하게 주입받기 위해 사용
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
     * listener가 미리 저장해둔 requestId를 바탕으로 request 엔티티 조회
     *
     * - processor는 각 item을 entity로 만들 때 request 객체가 필요함
     * - request_id만 context에 싣고, 실제 엔티티는 processor에서 조회하는 구조
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


    /**
     * process = DTO → Entity 변환 담당
     *
     * 여기서는 비즈니스 계산을 거의 하지 않고
     * 원본 item을 raw entity 형태로 옮기는 역할에 집중함
     *
     * request == null 이면
     * - 현재 partition은 처리할 데이터가 없는 상태
     * - null 반환하면 writer까지 가지 않고 skip됨
     */
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
