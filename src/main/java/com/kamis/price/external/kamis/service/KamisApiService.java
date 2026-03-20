package com.kamis.price.external.kamis.service;

import com.kamis.price.config.KamisApiProperties;
import com.kamis.price.external.kamis.client.KamisFeignClient;
import com.kamis.price.external.kamis.dto.KamisResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KamisApiService {

    private final KamisFeignClient kamisFeignClient;
    private final KamisApiProperties kamisApiProperties;

    /**
     * Kamis API 전체 응답 반환
     */
    public KamisResponseDto fetchCategoryPrices(String productClsCode, String categoryCode, String countryCode, String regDay) {

        KamisResponseDto response = kamisFeignClient.getPriceList(
                "dailyPriceByCategoryList",
                kamisApiProperties.getCertKey(),
                kamisApiProperties.getCertId(),
                "json",
                productClsCode,
                categoryCode,
                countryCode,
                regDay,
                "N"
        );

        return response;
    }

    public String getCertKey() {
        return kamisApiProperties.getCertKey();
    }

    public String getCertId() {
        return kamisApiProperties.getCertId();
    }

}
