package com.kamis.price.global.enums;

import lombok.Getter;

@Getter
public enum CountryCode {

    SEOUL("1101", "서울"),
    BUSAN("2100", "부산"),
    DAEGU("2200", "대구"),
    INCHEON("2300", "인천"),
    GWANGJU("2401", "광주"),
    DAEJEON("2501", "대전"),
    ULSAN("2601", "울산"),
    SUWON("3111", "수원"),
    GANGNEUNG("3214", "강릉"),
    CHUNCHEON("3211", "춘천"),
    CHEONGJU("3311", "청주"),
    JEONJU("3511", "전주"),
    POHANG("3711", "포항"),
    JEJU("3911", "제주");

    private final String code;
    private final String name;

    CountryCode(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
