package com.kamis.price.batch.kamis.job;


import com.kamis.price.batch.kamis.dto.ExpandedPriceRow;
import com.kamis.price.batch.kamis.processor.KamisItemProcessor;
import com.kamis.price.batch.kamis.reader.KamisItemReader;
import com.kamis.price.domain.price.entity.PriceData;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * KAMIS 가격 수집 Batch Job 설정
 */

@Configuration
@RequiredArgsConstructor
public class KamisPriceJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    private final KamisItemReader reader;
    private final KamisItemProcessor processor;
    private final JdbcBatchItemWriter<PriceData> writer;

    /**
     * Batch Job
     */
    @Bean
    public Job kamisPriceJob() {

        return new JobBuilder("kamisPriceJob", jobRepository)
                .start(kamisPriceStep())
                .build();
    }

    /**
     * Batch Step
     */
    @Bean
    public Step kamisPriceStep() {

        return new StepBuilder("kamisPriceStep", jobRepository)
                .<ExpandedPriceRow, PriceData>chunk(20, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}
