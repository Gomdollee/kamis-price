package com.kamis.price.batch.kamis;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class KamisBatchTest {

    @Resource
    private JobLauncher jobLauncher;

    @Resource
    private Job kamisPriceJob;

    @Test
    void runBatch() throws Exception {

        JobExecution execution =
                jobLauncher.run(
                        kamisPriceJob,
                        new JobParametersBuilder()
                                .addLong("time", System.currentTimeMillis())
                                .toJobParameters()
                );

        System.out.println("Batch Status = " + execution.getStatus());

    }



}
