package com.kamis.price.external.kamis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KamisConditionDto {

    @JsonProperty("p_product_cls_code")
    private String productClsCode;

    @JsonProperty("p_country_code")
    private String countryCode;

    @JsonProperty("p_regday")
    private String regday;

    @JsonProperty("p_convert_kg_yn")
    private String convertKgYn;

    @JsonProperty("p_category_code")
    private String categoryCode;

    @JsonProperty("p_cert_key")
    private String certKey;

    @JsonProperty("p_cert_id")
    private String certId;

    @JsonProperty("p_returntype")
    private String returnType;
}
