package com.kamis.price.domain.raw.service;

import com.kamis.price.batch.kamis.job.KamisRawJobConfig;
import com.kamis.price.batch.kamis.raw.processor.KamisRawProcessor;
import com.kamis.price.external.kamis.dto.KamisRawCollectCommand;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;

@SpringBootTest(
        properties = {
                "spring.task.scheduling.enabled=false",   // Scheduler 끄기
                "spring.batch.job.enabled=false"          // Batch 자동 실행 끄기
        }
)
class KamisRawServiceTest {

    @Autowired
    private KamisRawService service;

    // Batch 관련 Bean 강제 Mock 처리
    @MockitoBean
    private KamisRawProcessor kamisRawProcessor;

    @MockitoBean
    private KamisRawJobConfig kamisRawJobConfig;

    @Test
    void raw_수집_테스트() {

        var command = KamisRawCollectCommand.builder()
                .productClsCode("02")
                .categoryCode("200")
                .countryCode("1101")
                .regday(LocalDate.of(2015, 10, 1))
                .build();

        Long id = service.collectAndSave(command);

        System.out.println("requestId = " + id);
    }
}