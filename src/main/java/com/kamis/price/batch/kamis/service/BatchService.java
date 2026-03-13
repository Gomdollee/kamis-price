package com.kamis.price.batch.kamis.service;

import com.kamis.price.batch.kamis.dto.BatchConfigResponse;
import com.kamis.price.batch.kamis.dto.BatchRunResponse;
import com.kamis.price.batch.kamis.dto.BatchStatusResponse;
import com.kamis.price.batch.kamis.dto.JobStatusDto;
import com.kamis.price.config.KamisApiProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BatchService {

    private final JobLauncher jobLauncher;

    private final Job kamisPriceJob;

    private final JobExplorer jobExplorer;

    private final KamisApiProperties kamisApiProperties;

    /**
     * 배치 실행
     */
    public BatchRunResponse runBatch(String itemCategoryCode, String regDay) {

        System.out.println(" ?? ");

        try {

            JobParameters parameters =
                    new JobParametersBuilder()
                            .addString("itemCategoryCode", itemCategoryCode)
                            .addString("regDay", regDay)
                            .addLong("time", System.currentTimeMillis())
                            .toJobParameters();

            JobExecution execution = jobLauncher.run(kamisPriceJob, parameters);

            return BatchRunResponse.builder()
                    .jobId(execution.getJobId())
                    .status(execution.getStatus().name())
                    .startTime(execution.getStartTime())
                    .endTime(execution.getEndTime())
                    .itemCategoryCode(itemCategoryCode)
                    .regDay(regDay)
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
     * 배치 실행 이력 조회
     */
    public BatchStatusResponse getBatchStatus() {

        List<JobInstance> instances =
                jobExplorer.getJobInstances("kamisPriceJob", 0, 10);

        List<JobStatusDto> data = new ArrayList<>();

        for (JobInstance instance : instances) {

            List<JobExecution> executions =
                    jobExplorer.getJobExecutions(instance);

            for (JobExecution execution : executions) {

                Map<String, Object> params = new HashMap<>();

                execution.getJobParameters()
                        .getParameters()
                        .forEach((k, v) -> params.put(k, v.getValue()));

                data.add(
                        JobStatusDto.builder()
                                .jobInstanceId(instance.getInstanceId())
                                .jobName(execution.getJobInstance().getJobName())
                                .status(execution.getStatus().name())
                                .startTime(execution.getStartTime())
                                .endTime(execution.getEndTime())
                                .exitCode(execution.getExitStatus().getExitCode())
                                .params(params)
                                .build()
                );
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

    /**
     * API 설정 조회
     */
    public BatchConfigResponse getBatchConfig() {

        return BatchConfigResponse.builder()
                .apiConfigured(kamisApiProperties.isConfigured())
                .mockMode(kamisApiProperties.isMockMode())
                .baseUrl(kamisApiProperties.getBaseUrl())
                .certKeySet(kamisApiProperties.getCertKey() != null)
                .certIdSet(kamisApiProperties.getCertId() != null)
                .build();
    }

}



