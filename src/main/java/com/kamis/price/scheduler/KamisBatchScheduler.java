package com.kamis.price.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class KamisBatchScheduler {

    private final JobLauncher jobLauncher;

    @Qualifier("kamisPriceJob")
    private final Job kamisPriceJob;

    @Value("${kamis.default-category:100}")
    private String itemCategoryCode;

    /**
     * 매일 오전 9시
     */
    @Scheduled(cron = "${kamis.batch.schedule.daily-cron}")
    public void scheduleDailyPriceFetch() {

        try {

            log.info("KAMIS 일별 가격 배치 시작");

            String today = LocalDate.now().toString();

            JobParameters parameters =
                    new JobParametersBuilder()
                            .addString("itemCategoryCode", itemCategoryCode)
                            .addString("regDay", today)
                            .addLong("run.id", System.currentTimeMillis())
                            .toJobParameters();

            JobExecution execution =
                    jobLauncher.run(kamisPriceJob, parameters);

            log.info("KAMIS 배치 완료 status={}", execution.getStatus());

        } catch (Exception e) {

            log.error("KAMIS 배치 실행 실패", e);

        }
    }
}