package com.kamis.price.batch.kamis.job;

import com.kamis.price.batch.kamis.dto.ExpandedPriceRow;
import com.kamis.price.batch.kamis.processor.KamisItemProcessor;
import com.kamis.price.batch.kamis.reader.KamisMonthlyReader;
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

@Configuration
@RequiredArgsConstructor
public class KamisMonthlyPriceJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    private final KamisMonthlyReader reader;
    private final KamisItemProcessor processor;
    private final JdbcBatchItemWriter<PriceData> writer;

    @Bean
    public Job kamisMonthlyPriceJob() {

        return new JobBuilder("kamisMonthlyPriceJob", jobRepository)
                .start(kamisMonthlyPriceStep())
                .build();
    }

    @Bean
    public Step kamisMonthlyPriceStep() {

        return new StepBuilder("kamisMonthlyPriceStep", jobRepository)
                .<ExpandedPriceRow, PriceData>chunk(100, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}
