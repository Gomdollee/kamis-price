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
     * Batch Job 정의
     *
     * Job은 하나 이상의 Step으로 구성됨
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
     * Reader → Processor → Writer 흐름
     */
    @Bean
    public Step kamisPriceStep() {

        return new StepBuilder("kamisPriceStep", jobRepository)
                .<ExpandedPriceRow, PriceData>chunk(100, transactionManager)

                // 데이터 읽기
                .reader(reader)

                // 데이터 변환
                .processor(processor)

                // DB 저장
                .writer(writer)

                .build();
    }
}
