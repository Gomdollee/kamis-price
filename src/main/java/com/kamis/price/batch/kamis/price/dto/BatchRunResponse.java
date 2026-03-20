package com.kamis.price.batch.kamis.price.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * 배치 실행 요청 이후 결과를 응답하기 위한 DTO
 * 응답 예시
 *{
 *   "success": true,
 *   "message": "배치 실행 완료",
 *   "jobId": 1,
 *   "status": "COMPLETED",
 *   "startTime": "2024-01-15T10:30:00",
 *   "endTime": "2024-01-15T10:30:05",
 *   "itemCategoryCode": "200",
 *   "regDay": "2024-01-15",
 *   "mockMode": false
 * }
 *
 */
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
