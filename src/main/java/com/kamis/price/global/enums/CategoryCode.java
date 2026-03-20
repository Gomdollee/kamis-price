package com.kamis.price.global.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CategoryCode {

    GRAIN("100", "식량작물"),
    VEGETABLE("200", "채소류"),
    SPECIAL("300", "특용작물"),
    FRUIT("400", "과일류"),
    LIVESTOCK("500", "축산물"),
    FISHERY("600", "수산물");

    private final String code;
    private final String description;
}
