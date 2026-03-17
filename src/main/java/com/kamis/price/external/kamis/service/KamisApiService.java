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

        validateResponse(response);

        return response;
    }

    /**
     * 응답 검증
     */
    public void validateResponse(KamisResponseDto response) {

        if (response == null) {
            throw new RuntimeException("KAMIS API response is null");
        }

        if (response.getData() == null) {
            throw new RuntimeException("KAMIS API data is null");
        }

        if (!"000".equals(response.getData().getErrorCode())) {
            throw new RuntimeException("KAMIS API error: " + response.getData().getErrorCode());
        }

        if (response.getData().getItem() == null) {
            throw new RuntimeException("KAMIS API item is null");
        }
    }

}
