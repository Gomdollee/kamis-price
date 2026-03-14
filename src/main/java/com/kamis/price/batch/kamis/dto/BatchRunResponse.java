package com.kamis.price.batch.kamis.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BatchRunResponse {

    private boolean success;
    private String message;

    private Long jobId;
    private String status;

    private Object startTime;
    private Object endTime;

    private String itemCategoryCode;
    private String regDay;

    private boolean mockMode;

}
