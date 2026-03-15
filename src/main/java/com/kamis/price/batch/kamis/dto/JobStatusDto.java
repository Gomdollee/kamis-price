package com.kamis.price.batch.kamis.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;


/**
 * 배치 Job 1건의 실행 상태를 담는 DTO
 *
 * 응답 예시
 *
 *    {
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
 */
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
