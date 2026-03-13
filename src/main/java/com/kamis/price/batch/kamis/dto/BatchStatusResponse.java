package com.kamis.price.batch.kamis.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BatchStatusResponse {

    private boolean success;
    private int count;

    private boolean mockMode;
    private boolean apiConfigured;

    private List<JobStatusDto> data;
}
