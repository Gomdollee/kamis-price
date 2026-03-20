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

        try {

            JobParameters parameters =
                    new JobParametersBuilder()
                            .addString("itemCategoryCode", itemCategoryCode)
                            .addString("regDay", regDay)
                            .toJobParameters();

            JobExecution execution = jobLauncher.run(kamisPriceJob, parameters);

            return BatchRunResponse.builder()
                    .success(true)
                    .message("배치 실행 완료")
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

        // 최근 JobInstance 조회 (최대10개)
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



