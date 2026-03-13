package com.kamis.price.external.kamis.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

/**
 * 실제 가격 데이터
 */

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KamisItem {

    private String item_name;   // 품목명
    private String item_code;   // 품목코드

    private String kind_name;   // 품종명
    private String kind_code;   // 품종코드

    private String rank;        // 상태(상품, 중품)
    private String unit;        // 단위

    private String day1;        // 일자(조회일자)
    private String dpr1;        // 조회일자 가격

    private String day2;        // 1일전 일자(조회일자 기준)
    private String dpr2;        // 1일전 가격

    private String day3;        // 1주일전 일자(조회일자 기준)
    private String dpr3;        // 1주일전 가격

    private String day4;        // 2주일전 일자(조회일자 기준)
    private String dpr4;        // 2주일전 가격

    private String day5;        // 1개월전 일자(조회일자 기준)
    private String dpr5;        // 1개월전 가격

    private String day6;        // 1년전 일자(조회일자 기준)
    private String dpr6;        // 1년전 가격

    private String day7;        // 평년일자
    private String dpr7;        // 평년 가격


}
