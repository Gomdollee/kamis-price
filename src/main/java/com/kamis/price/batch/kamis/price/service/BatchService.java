package com.kamis.price.batch.kamis.price.service;

import com.kamis.price.batch.kamis.price.dto.BatchConfigResponse;
import com.kamis.price.batch.kamis.price.dto.BatchRunResponse;
import com.kamis.price.batch.kamis.price.dto.BatchStatusResponse;
import com.kamis.price.batch.kamis.price.dto.JobStatusDto;
import com.kamis.price.config.KamisApiProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BatchService {

    private final JobLauncher jobLauncher;
    private final JobExplorer jobExplorer;
    private final KamisApiProperties kamisApiProperties;

    private final Job kamisPriceJob;

    public BatchService(
            JobLauncher jobLauncher,
            @Qualifier("kamisPriceJob") Job kamisPriceJob,
            JobExplorer jobExplorer,
            KamisApiProperties kamisApiProperties
    ) {
        this.jobLauncher = jobLauncher;
        this.kamisPriceJob = kamisPriceJob;
        this.jobExplorer = jobExplorer;
        this.kamisApiProperties = kamisApiProperties;
    }

    /**
     * 배치 실행
     */
    public BatchRunResponse runBatch() {

        try {

            JobParameters parameters =
                    new JobParametersBuilder()
                            .addLong("timestamp", System.currentTimeMillis())
                            .toJobParameters();

            JobExecution execution = jobLauncher.run(kamisPriceJob, parameters);

            return BatchRunResponse.builder()
                    .success(true)
                    .message("배치 실행 완료")
                    .jobId(execution.getJobId())
                    .status(execution.getStatus().name())
                    .startTime(execution.getStartTime())
                    .endTime(execution.getEndTime())
                    .mockMode(kamisApiProperties.isMockMode())
                    .build();

        } catch (Exception e) {

            return BatchRunResponse.builder()
                    .success(false)
                    .message("배치 실행 실패: " + e.getMessage())
                    .build();
        }
    }

    /**
     * 🔥 배치 실행 이력 조회 (복구)
     */
    public BatchStatusResponse getBatchStatus() {

        List<JobInstance> instances =
                jobExplorer.getJobInstances("kamisPriceJob", 0, 10);

        List<JobStatusDto> data = new ArrayList<>();

        for (JobInstance instance : instances) {

            List<JobExecution> executions =
                    jobExplorer.getJobExecutions(instance);

            for (JobExecution execution : executions) {
                data.add(convertToDto(instance, execution));
            }
        }

        return BatchStatusResponse.builder()
                .success(true)
                .count(data.size())
                .mockMode(kamisApiProperties.isMockMode())
                .apiConfigured(kamisApiProperties.isConfigured())
                .data(data)
                .build();
    }

    private JobStatusDto convertToDto(
            JobInstance instance,
            JobExecution execution
    ) {

        Map<String, Object> params = new HashMap<>();

        execution.getJobParameters()
                .getParameters()
                .forEach((k, v) -> params.put(k, v.getValue()));

        return JobStatusDto.builder()
                .jobInstanceId(instance.getInstanceId())
                .jobName(instance.getJobName())
                .status(execution.getStatus().name())
                .startTime(execution.getStartTime())
                .endTime(execution.getEndTime())
                .exitCode(execution.getExitStatus().getExitCode())
                .params(params)
                .build();
    }
}

