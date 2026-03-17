package com.kamis.price.external.kamis.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KamisRequestDto {

    private String productClsCode;
    private String categoryCode;
    private String countryCode;
    private String regDay;
}
