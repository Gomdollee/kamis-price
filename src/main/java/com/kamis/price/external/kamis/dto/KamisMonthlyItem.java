package com.kamis.price.external.kamis.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

/**
 * 월별 가격 데이터 DTO
 *
 * item 배열 내부 객체
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KamisMonthlyItem {

    /**
     * 연도
     */
    private String yyyy;

    /**
     * 1월 가격
     */
    private String m1;

    /**
     * 2월 가격
     */
    private String m2;

    /**
     * 3월 가격
     */
    private String m3;

    /**
     * 4월 가격
     */
    private String m4;

    /**
     * 5월 가격
     */
    private String m5;

    /**
     * 6월 가격
     */
    private String m6;

    /**
     * 7월 가격
     */
    private String m7;

    /**
     * 8월 가격
     */
    private String m8;

    /**
     * 9월 가격
     */
    private String m9;

    /**
     * 10월 가격
     */
    private String m10;

    /**
     * 11월 가격
     */
    private String m11;

    /**
     * 12월 가격
     */
    private String m12;

    /**
     * 연평균 가격
     */
    private String yearavg;
}
