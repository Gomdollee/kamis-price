package com.kamis.price.external.kamis.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
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
public class KamisResponse {
    
    private Data data;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        private List<KamisItem> item;
    }
}
