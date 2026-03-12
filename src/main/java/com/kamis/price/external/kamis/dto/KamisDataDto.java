package com.kamis.price.external.kamis.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KamisDataDto {

    private String error_code;

    private List<KamisItemDto> item;
}
