package com.kamis.price.domain.price.entity;

import jakarta.persistence.*;
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
@Setter
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
     * 등급 코드
     */
    @Column(name = "rank_code")
    private String rankCode;

    /**
     * 등급명
     */
    @Column(name = "rank_name")
    private String rankName;

    /**
     * 지역명
     */
    @Column(name = "county_name")
    private String countyName;

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

    /**
     * 데이터 저장 시 자동 실행
     */
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }


}
