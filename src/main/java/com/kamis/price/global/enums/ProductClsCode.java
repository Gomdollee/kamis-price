package com.kamis.price.global.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductClsCode {

    RETAIL("01", "소매"),
    WHOLESALE("02", "도매");

    private final String code;
    private final String description;
}
