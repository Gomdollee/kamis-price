package com.kamis.price.external.kamis.service;

import com.kamis.price.config.KamisApiProperties;
import com.kamis.price.external.kamis.client.KamisFeignClient;
import com.kamis.price.external.kamis.dto.KamisItem;
import com.kamis.price.external.kamis.dto.KamisMonthlyItem;
import com.kamis.price.external.kamis.dto.KamisMonthlyResponse;
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
    private final KamisApiProperties kamisApiProperties;

    /**
     * 부류별 가격 API 호출
     *
     * @param productClsCode 01: 소매, 02: 도매
     * @param categoryCode   100~600
     * @param regDay         yyyy-MM-dd
     */
    public List<KamisItem> fetchCategoryPrices(String productClsCode, String categoryCode, String countryCode, String regDay) {

        if (kamisApiProperties.isMockMode()) {
            return createMockData();
        }

        try {

            KamisResponse response = kamisFeignClient.getPriceList(
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

            if (response == null || response.getData() == null || response.getData().getItem() == null) {
                return Collections.emptyList();
            }

            return response.getData().getItem();

        } catch (Exception e) {

            return Collections.emptyList();
        }

    }

    /**
     * Mock 데이터 생성
     */
    private List<KamisItem> createMockData() {
        return List.of();
    }

    /**
     * 월별 도.소매가격정보 (※ 축평원 데이터는 제외됨)
     */
    public List<KamisMonthlyItem> fetchMonthlyPrices(
            String categoryCode,
            String yyyy
    ) {

        KamisMonthlyResponse response =
                kamisFeignClient.fetchMonthlyPrices(
                        "monthlySalesList",
                        kamisApiProperties.getCertKey(),
                        kamisApiProperties.getCertId(),
                        "json",
                        yyyy,
                        categoryCode
                );

        if (response == null
                || response.getPrice() == null
                || response.getPrice().getPrice() == null) {

            return Collections.emptyList();
        }

        return response.getPrice().getPrice();
    }

}
