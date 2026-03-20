package com.kamis.price.external.kamis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KamisItemDto {

    @JsonProperty("item_name")
    private String itemName;

    @JsonProperty("item_code")
    private String itemCode;

    @JsonProperty("kind_name")
    private String kindName;

    @JsonProperty("kind_code")
    private String kindCode;

    @JsonProperty("rank")
    private String rank;

    @JsonProperty("rank_code")
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
}