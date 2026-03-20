package com.kamis.price.external.kamis.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class KamisResponseDto {

    private List<KamisConditionDto> condition;
    private KamisDataDto data;
}
