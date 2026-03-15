package com.kamis.price.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Batch 기능 활성화 설정
 *
 * @EnableBatchProcessing
 *
 * 해당 어노테이션을 통해 Spring Batch의 핵심 구성요소들 자동 Bean 등록
 *
 * 주요 자동 등록 Bean
 * - JobRepository : Batch 실행 메타데이터 저장
 * - JobLauncher : Batch 실행
 * - JobExplorer : Batch 실행 이력 조회
 * - JobBuilderFactory : Job 생성 지원
 * - StepBuilderFactory : Step 생성 지원
 *
 */
@Configuration
@EnableBatchProcessing
public class BatchConfig {
}
