package com.kamis.price.domain.price.entity;

import com.kamis.price.domain.raw.entity.KamisRawItem;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 외부 KAMIS API에서 받은 가격 데이터를
 * DB에 저장하기 위한 Entity
 */

@Entity
@Table(name = "price_data")
@Getter
@NoArgsConstructor
public class PriceData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 데이터 출처
     *
     * CATEGORY : 부류별 가격 API
     * ITEM : 품목별 가격 API
     */
    @Column(name = "source_type")
    private String sourceType;

    /**
     * 품목 코드
     */
    @Column(name = "item_code")
    private String itemCode;

    /**
     * 품목명
     */
    @Column(name = "item_name")
    private String itemName;

    /**
     * 품종 코드
     */
    @Column(name = "kind_code")
    private String kindCode;

    /**
     * 품종명
     */
    @Column(name = "kind_name")
    private String kindName;

    /**
     * 등급
     */
    @Column(name = "rank")
    private String rank;

    /**
     * 지역 코드
     */
    @Column(name = "country_code")
    private String countryCode;

    /**
     * 지역명
     */
    @Column(name = "country_name")
    private String countryName;

    /**
     * 시장명
     */
    @Column(name = "market_name")
    private String marketName;

    /**
     * 단위
     *
     * 예
     * 10kg
     * 1kg
     */
    private String unit;

    /**
     * 가격
     *
     * KAMIS는 "5,500" 형태로 주기 때문에
     * 콤마 제거 후 Integer로 저장합니다
     */
    private Integer price;

    /**
     * 가격 기준 날짜
     *
     * 예
     * 2025-03-10
     */
    @Column(name = "reg_day")
    private LocalDate regDay;

    /**
     * 가격 타입
     *
     * 예
     * 당일
     * 1일전
     * 1주일전
     */
    @Column(name = "price_type")
    private String priceType;

    /**
     * 생성 시간
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Builder
    private PriceData(
            String sourceType,
            String itemCode,
            String itemName,
            String kindCode,
            String kindName,
            String rank,
            String countryCode,
            String countryName,
            String marketName,
            String unit,
            Integer price,
            LocalDate regDay,
            String priceType
    ) {
        this.sourceType = sourceType;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.kindCode = kindCode;
        this.kindName = kindName;
        this.rank = rank;
        this.countryCode = countryCode;
        this.countryName = countryName;
        this.marketName = marketName;
        this.unit = unit;
        this.price = price;
        this.regDay = regDay;
        this.priceType = priceType;
        this.createdAt = LocalDateTime.now();
    }

    public static PriceData from(KamisRawItem item, int price) {
        return PriceData.builder()
                .sourceType("CATEGORY")
                .itemCode(item.getItemCode())
                .itemName(item.getItemName())
                .kindCode(item.getKindCode())
                .kindName(item.getKindName())
                .rank(item.getRank())
                .countryCode(item.getCountryCode())
                .countryName(null) // 필요하면 enum 매핑
                .marketName(null)
                .unit(item.getUnit())
                .price(price)
                .regDay((item.getRegday()))
                .priceType("DAY1")
                .build();
    }


}
