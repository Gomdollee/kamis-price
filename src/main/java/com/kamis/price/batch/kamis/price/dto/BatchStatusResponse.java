package com.kamis.price.batch.kamis.price.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 배치 Job 상태 목록 조회 응답 DTO
 *
 * 응답 예시
 *
 * {
 *   "success": true,
 *   "count": 2,
 *   "mockMode": true,
 *   "apiConfigured": false,
 *   "data": [
 *     {
 *       "jobInstanceId": 2,
 *       "jobName": "kamisPriceJob",
 *       "status": "COMPLETED",
 *       "startTime": "2024-01-15T10:35:00",
 *       "endTime": "2024-01-15T10:35:03",
 *       "exitCode": "COMPLETED",
 *       "params": {
 *         "itemCategoryCode": "200",
 *         "regDay": "2024-01-15"
 *       }
 *     }
 *   ]
 * }
 */
@Getter
@Builder
public class BatchStatusResponse {

    private boolean success;
    private int count;

    private boolean mockMode;
    private boolean apiConfigured;

    private List<JobStatusDto> data;
}
