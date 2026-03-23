package com.kamis.price.batch.kamis.raw.job;

import com.kamis.price.batch.kamis.raw.listener.KamisStepListener;
import com.kamis.price.batch.kamis.raw.partition.KamisPartitioner;
import com.kamis.price.batch.kamis.raw.processor.KamisRawProcessor;
import com.kamis.price.batch.kamis.raw.reader.KamisRawReader;
import com.kamis.price.batch.kamis.raw.writer.KamisRawWriter;
import com.kamis.price.domain.raw.entity.KamisRawItem;
import com.kamis.price.domain.raw.entity.KamisRawRequest;
import com.kamis.price.external.kamis.dto.KamisItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class KamisRawJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    private final KamisPartitioner partitioner;
    private final KamisRawReader reader;
    private final KamisRawProcessor processor;
    private final KamisRawWriter writer;
    private final KamisStepListener stepListener;

    /**
     * Job = 배치 작업 전체 단위
     *
     * 이 프로젝트에서는 "KAMIS 원본 데이터를 수집하는 전체 작업" 자체가 하나의 Job
     *
     * 흐름:
     * kamisRawJob
     *   -> masterStep
     *        -> partitioner가 여러 partition 생성
     *        -> 각 partition마다 kamisRawStep 실행
     */
    @Bean
    public Job kamisRawJob() {
        return new JobBuilder("kamisRawJob", jobRepository)
                .start(masterStep())   // 🔥 변경
                .build();
    }

    /**
     * masterStep = 실제 데이터 처리 step을 병렬로 나눠서 실행시키는 상위 step
     *
     * partitioner("kamisRawStep", partitioner)
     * - kamisRawStep 이라는 worker step을
     * - partitioner가 생성한 execution context 개수만큼 분리 실행
     *
     * gridSize(10)
     * - 병렬 실행 힌트 값
     * - 보통 동시에 처리할 스레드 수 또는 분할 규모
     * - 단, 실제 병렬 실행 여부는 TaskExecutor 설정 유무에도 영향 받음
     *
     * 중요:
     * 코드는 partitioner + gridSize를 사용하고 있으므로
     * "논리적으로 partition 적용은 되어 있음"
     */
    @Bean
    public Step masterStep() {
        return new StepBuilder("masterStep", jobRepository)
                .partitioner("kamisRawStep", partitioner)
                .step(kamisRawStep())
                .gridSize(10)
                .build();
    }

    /**
     * kamisRawStep = 각 partition이 실제로 수행하는 작업 step
     */
    @Bean
    public Step kamisRawStep() {
        return new StepBuilder("kamisRawStep", jobRepository)
                .<KamisItemDto, KamisRawItem>chunk(100, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .listener(stepListener)
                .faultTolerant()
                .skipLimit(10)
                .skip(Exception.class)
                .build();
    }
}
