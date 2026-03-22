package com.kamis.price.batch.kamis.raw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class KamisRawBatchService {

    private final JobLauncher jobLauncher;
    private final Job kamisRawJob;

    public KamisRawBatchService(
            JobLauncher jobLauncher,
            @Qualifier("kamisRawJob") Job kamisRawJob
    ) {
        this.jobLauncher = jobLauncher;
        this.kamisRawJob = kamisRawJob;
    }

    /**
     * 전체 raw 수집 배치를 실행합니다.
     * regday만 받으면, 나머지 조건은 partitioner가 전부 생성
     */
    public JobExecution run(String regday) throws Exception {
        JobParameters parameters = new JobParametersBuilder()
                .addString("regday", regday)
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters();

        return jobLauncher.run(kamisRawJob, parameters);
    }
}
