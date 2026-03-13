package com.kamis.price.batch.kamis.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BatchConfigResponse {

    private boolean apiConfigured;
    private boolean mockMode;

    private String baseUrl;

    private boolean certKeySet;
    private boolean certIdSet;
}
