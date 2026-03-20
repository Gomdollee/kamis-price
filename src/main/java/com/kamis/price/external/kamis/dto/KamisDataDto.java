package com.kamis.price.external.kamis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class KamisDataDto {

    @JsonProperty("error_code")
    private String errorCode;

    private List<KamisItemDto> item;
}
