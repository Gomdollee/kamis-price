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
     * Job 정의
     */
    @Bean
    public Job kamisRawJob() {
        return new JobBuilder("kamisRawJob", jobRepository)
                .start(masterStep())   // 🔥 변경
                .build();
    }

    /**
     * Master Step (Partition)
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
     * 기존 Step 그대로 유지
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
