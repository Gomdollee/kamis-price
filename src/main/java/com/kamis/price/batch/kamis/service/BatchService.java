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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BatchService {

    private final JobLauncher jobLauncher;
    private final Job kamisPriceJob;
    private final Job kamisMonthlyPriceJob;
    private final JobExplorer jobExplorer;
    private final KamisApiProperties kamisApiProperties;

    public BatchService(
            JobLauncher jobLauncher,
            @Qualifier("kamisPriceJob") Job kamisPriceJob,
            @Qualifier("kamisMonthlyPriceJob") Job kamisMonthlyPriceJob,
            JobExplorer jobExplorer,
            KamisApiProperties kamisApiProperties
    ) {
        this.jobLauncher = jobLauncher;
        this.kamisPriceJob = kamisPriceJob;
        this.kamisMonthlyPriceJob = kamisMonthlyPriceJob;
        this.jobExplorer = jobExplorer;
        this.kamisApiProperties = kamisApiProperties;
    }


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

    /**
     * * 월별 도.소매가격정보 (※ 축평원 데이터는 제외됨)
     */
    public BatchRunResponse runMonthlyBatch(
            String itemCategoryCode,
            String yyyy
    ) {

        try {

            JobParameters parameters =
                    new JobParametersBuilder()
                            .addString("itemCategoryCode", itemCategoryCode)
                            .addString("yyyy", yyyy)
                            .addLong("run.id", System.currentTimeMillis())
                            .toJobParameters();

            JobExecution execution =
                    jobLauncher.run(kamisMonthlyPriceJob, parameters);

            return BatchRunResponse.builder()
                    .success(true)
                    .message("월별 배치 실행 완료")
                    .jobId(execution.getJobId())
                    .status(execution.getStatus().name())
                    .build();

        } catch (Exception e) {

            return BatchRunResponse.builder()
                    .success(false)
                    .message("월별 배치 실행 실패")
                    .build();
        }
    }


}



