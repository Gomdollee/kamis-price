package com.kamis.price.external.kamis.service;

import com.kamis.price.external.kamis.client.KamisFeignClient;
import com.kamis.price.external.kamis.dto.KamisItem;
import com.kamis.price.external.kamis.dto.KamisResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KamisApiService {

    private final KamisFeignClient kamisFeignClient;

    @Value("${kamis.api.key}")
    private String apiKey;

    @Value("${kamis.api.id}")
    private String apiId;

    @Value("${kamis.mock:false}")
    private boolean mock;

    /**
     * 부류별 가격 API 호출
     *
     * @param productClsCode 01: 소매, 02: 도매
     * @param categoryCode   100~600
     * @param regDay         yyyy-MM-dd
     */
    public List<KamisItem> fetchCategoryPrices(String productClsCode, String categoryCode, String countryCode, String regDay) {

        if (mock) {
            return createMockData();
        }

        try {

            KamisResponse response = kamisFeignClient.getPriceList(
                    "dailyPriceByCategoryList",
                    apiKey,
                    apiId,
                    "json",
                    productClsCode,
                    categoryCode,
                    countryCode,
                    regDay,
                    "N"
            );

            if (response == null || response.getData() == null || response.getData().getItem() == null) {
                return Collections.emptyList();
            }

            return response.getData().getItem();

        } catch (Exception e) {

            return Collections.emptyList();
        }

    }

    private List<KamisItem> createMockData() {
        return List.of();
    }
}
