package com.kamis.price.batch.kamis.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * KAMIS item 1건에서 dpr1~dpr7 가격을 펼쳐서
 * DB 저장 단위(row)로 변환한 중간 DTO
 */

@Getter
@Builder
public class ExpandedPriceRow {

    private String sourceType; // CATEGORY , ITEM

    private String itemCode;
    private String itemName;

    private String kindCode;
    private String kindName;

    private String rank;

    private String countryCode;
    private String countryName;

    private String marketName;

    private String unit;

    /**
     * "5,500" 형태 원본 가격 문자열
     */
    private String priceRaw;

    /**
     * 당일, 1일전, 1주일전, 2주일전, 1개월전, 1년전, 일평년
     */
    private String priceType;

    /**
     * 기준 날짜 (yyyy-MM-dd)
     */
    private String regDay;
}
