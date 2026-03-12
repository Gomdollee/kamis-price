package com.kamis.price.external.kamis.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.util.List;

/**
 * KAMIS 전체 응답 구조
 *
 * {
 *   condition: []
 *   data: { }
 * }
 */

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KamisResponseDto {
    
    // 요청 조건 정보
    private List<KamisConditionDto> condition;

    // 실제 데이터 영역
    private KamisDataDto data;
}
