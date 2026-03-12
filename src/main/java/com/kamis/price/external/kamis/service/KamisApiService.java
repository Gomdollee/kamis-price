package com.kamis.price.external.kamis.service;

import com.kamis.price.external.kamis.client.KamisFeignClient;
import com.kamis.price.external.kamis.dto.KamisItemDto;
import com.kamis.price.external.kamis.dto.KamisResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KamisApiService {

    private final KamisFeignClient kamisFeignClient;

    @Value("${kamis.api.key}")
    private String apiKey;

    @Value("${kamis.api.id}")
    private String apiId;

    /**
     * KAMIS 가격 데이터 조회
     */
    public List<KamisItemDto> fetchPrices() {

        KamisResponseDto responseDto =
                kamisFeignClient.getPriceList(
                        "dailyPriceByCategoryList",
                        apiKey,
                        apiId,
                        "json",
                        "02",
                        "200",
                        "1101",
                        "2026-03-11",
                        "N"
                );

        return responseDto.getData().getItem();
    }
}
