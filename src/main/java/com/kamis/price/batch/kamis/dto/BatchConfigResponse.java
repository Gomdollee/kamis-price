package com.kamis.price.batch.kamis.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * 배치 실행에 필요한 KAMIS API 환경 설정 상태를 내려주는 응답 DTO
 *
 * 응답 예시
 *
 * {
 *   "apiConfigured": false,
 *   "mockMode": true,
 *   "baseUrl": "https://www.kamis.or.kr/service/price/xml.do",
 *   "certKeySet": false,
 *   "certIdSet": false
 * }
 */
@Getter
@Builder
public class BatchConfigResponse {

    private boolean apiConfigured;
    private boolean mockMode;

    private String baseUrl;

    private boolean certKeySet;
    private boolean certIdSet;
}
