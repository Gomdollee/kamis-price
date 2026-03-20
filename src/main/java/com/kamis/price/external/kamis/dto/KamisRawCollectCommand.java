package com.kamis.price.external.kamis.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

/**
 *  수집용 DTO
 */
@Getter
@Builder
public class KamisRawCollectCommand {

    private String productClsCode;
    private String categoryCode;
    private String countryCode;
    private LocalDate regday;
}
