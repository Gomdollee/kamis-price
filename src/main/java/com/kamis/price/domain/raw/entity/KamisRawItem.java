package com.kamis.price.domain.raw.entity;

import com.kamis.price.external.kamis.dto.KamisItemDto;
import com.kamis.price.global.enums.RawStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "kamis_raw_item",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {
                        "item_code", "kind_code", "rank_code",
                        "regday", "country_code"
                }
        )
)

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KamisRawItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id")
    private KamisRawRequest request;

    private String countryCode;
    private String regday;
    private String categoryCode;

    private String itemName;
    private String itemCode;

    private String kindName;
    private String kindCode;

    private String rank;
    private String rankCode;

    private String unit;

    private String day1;
    private String dpr1;

    private String day2;
    private String dpr2;

    private String day3;
    private String dpr3;

    private String day4;
    private String dpr4;

    private String day5;
    private String dpr5;

    private String day6;
    private String dpr6;

    private String day7;
    private String dpr7;

    @Lob
    private String rawJson;

    @Enumerated(EnumType.STRING)
    private RawStatus processingStatus;

    private int processingRetryCount;
    private String processingErrorMessage;

    private LocalDateTime createdAt;

    @Builder
    private KamisRawItem(
            KamisRawRequest request,
            String categoryCode,
            String countryCode,
            String regday,
            String itemName,
            String itemCode,
            String kindName,
            String kindCode,
            String rank,
            String rankCode,
            String unit,
            String day1, String dpr1,
            String day2, String dpr2,
            String day3, String dpr3,
            String day4, String dpr4,
            String day5, String dpr5,
            String day6, String dpr6,
            String day7, String dpr7,
            String rawJson
    ) {
        this.request = request;
        this.categoryCode = categoryCode;
        this.countryCode = countryCode;
        this.regday = regday;

        this.itemName = itemName;
        this.itemCode = itemCode;
        this.kindName = kindName;
        this.kindCode = kindCode;
        this.rank = rank;
        this.rankCode = rankCode;
        this.unit = unit;

        this.day1 = day1;
        this.dpr1 = dpr1;
        this.day2 = day2;
        this.dpr2 = dpr2;
        this.day3 = day3;
        this.dpr3 = dpr3;
        this.day4 = day4;
        this.dpr4 = dpr4;
        this.day5 = day5;
        this.dpr5 = dpr5;
        this.day6 = day6;
        this.dpr6 = dpr6;
        this.day7 = day7;
        this.dpr7 = dpr7;

        this.rawJson = rawJson;
        this.processingStatus = RawStatus.READY;
        this.createdAt = LocalDateTime.now();
    }

    public static KamisRawItem from(
            KamisRawRequest request,
            KamisItemDto item,
            String categoryCode,
            String countryCode,
            String regday,
            String rawJson
    ) {
        return KamisRawItem.builder()
                .request(request)
                .categoryCode(categoryCode)
                .countryCode(countryCode)
                .regday(regday)
                .itemName(item.getItemName())
                .itemCode(item.getItemCode())
                .kindName(item.getKindName())
                .kindCode(item.getKindCode())
                .rank(item.getRank())
                .rankCode(item.getRankCode())
                .unit(item.getUnit())
                .day1(item.getDay1())
                .dpr1(item.getDpr1())
                .day2(item.getDay2())
                .dpr2(item.getDpr2())
                .day3(item.getDay3())
                .dpr3(item.getDpr3())
                .day4(item.getDay4())
                .dpr4(item.getDpr4())
                .day5(item.getDay5())
                .dpr5(item.getDpr5())
                .day6(item.getDay6())
                .dpr6(item.getDpr6())
                .day7(item.getDay7())
                .dpr7(item.getDpr7())
                .rawJson(rawJson)
                .build();
    }

    public void markProcessed() {
        this.processingStatus = RawStatus.PROCESSED;
    }

    public void markFailed(String message) {
        this.processingStatus = RawStatus.FAILED;
        this.processingRetryCount++;
        this.processingErrorMessage = message;
    }

}
