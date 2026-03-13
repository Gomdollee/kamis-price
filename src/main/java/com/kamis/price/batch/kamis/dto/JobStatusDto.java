package com.kamis.price.batch.kamis.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class JobStatusDto {

    private Long jobInstanceId;
    private String jobName;
    private String status;

    private Object startTime;
    private Object endTime;

    private String exitCode;

    private Map<String, Object> params;
}
