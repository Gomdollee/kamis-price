package com.kamis.price.external.kamis.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.util.List;


/**
 * KAMIS 월별 가격 API 응답 DTO
 *
 * monthlySalesList 응답 구조
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KamisMonthlyResponse {

    /**
     * 응답 코드
     */
    private String error_code;

    /**
     * 가격 데이터 영역
     */
    private Price price;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Price {

        /**
         * 도매 / 소매 구분
         */
        private String productclscode;

        /**
         * 설명 정보
         */
        private String caption;

        /**
         * 연도별 가격 리스트
         */
        private List<KamisMonthlyItem> price;
    }
}
