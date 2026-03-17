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
 * KAMIS 가격 수집 Batch Job 설정 클래스
 *
 * Spring Batch 기본 구조
 *
 * Job
 *  └ Step
 *      └ Reader → Processor → Writer
 *
 * Reader
 *  - 외부 API / 파일 / DB 등에서 데이터 읽기
 *
 * Processor
 *  - 읽은 데이터를 가공 / 변환
 *
 * Writer
 *  - DB 또는 다른 저장소에 데이터 저장
 */

@Configuration
@RequiredArgsConstructor
public class KamisPriceJobConfig {


    /**
     * Spring Batch 메타데이터 저장소
     * JobExecution / StepExecution 등의 상태 관리
     */
    private final JobRepository jobRepository;

    /**
     * 트랜잭션 관리자
     * chunk 단위로 commit/rollback 처리
     */
    private final PlatformTransactionManager transactionManager;

    /**
     * 데이터 읽기 (KAMIS API 호출)
     */
    private final KamisItemReader reader;

    /**
     * 데이터 가공 (DTO → Entity 변환)
     */
    private final KamisItemProcessor processor;

    /**
     * 데이터 저장 (DB insert)
     */
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
     * chunk 기반 처리
     *
     * <ExpandedPriceRow, PriceData>
     *
     * Reader가 ExpandedPriceRow 반환
     * Processor가 PriceData로 변환
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
