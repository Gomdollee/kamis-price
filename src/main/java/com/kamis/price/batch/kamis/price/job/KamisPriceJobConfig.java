package com.kamis.price.batch.kamis.price.job;


import com.kamis.price.batch.kamis.price.processor.KamisProcessor;
import com.kamis.price.batch.kamis.price.reader.KamisItemReader;
import com.kamis.price.batch.kamis.price.writer.PriceWriter;
import com.kamis.price.domain.price.entity.PriceData;
import com.kamis.price.domain.raw.entity.KamisRawItem;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class KamisPriceJobConfig {


    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    private final KamisItemReader reader;
    private final KamisProcessor processor;
    private final PriceWriter writer;

    /**
     * Job 정의
     */
    @Bean
    public Job kamisPriceJob() {

        return new JobBuilder("kamisPriceJob", jobRepository)
                .start(kamisPriceStep())
                .build();
    }

    /**
     * Step 정의
     *
     * <KamisRawItem, List<PriceData>>
     *
     * Reader: KamisRawItem
     * Processor: List<PriceData>
     * Writer: flatten 후 저장
     */
    @Bean
    public Step kamisPriceStep() {

        return new StepBuilder("kamisPriceStep", jobRepository)
                .<KamisRawItem, List<PriceData>>chunk(100, transactionManager)

                // 데이터 읽기
                .reader(reader)

                // 데이터 가공 (1 → N)
                .processor(processor)

                // DB 저장
                .writer(writer)

                .build();
    }
}
