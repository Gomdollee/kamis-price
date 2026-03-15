package com.kamis.price.controller;

import com.kamis.price.domain.price.entity.PriceData;
import com.kamis.price.domain.price.service.PriceQueryService;
import com.kamis.price.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 가격 조회 API를 제공하는 Controller
 */
@RestController
@RequestMapping("/api/prices")
@RequiredArgsConstructor
public class PriceController {

    private final PriceQueryService service;

    /**
     * 날짜 기준 가격 조회
     *
     * API
     * GET /api/prices?regDay=yyyy-MM-dd
     */
    @GetMapping
    public ApiResponse<List<PriceData>> getPricesByDate(@RequestParam(name = "regDay") String regDay) {

        List<PriceData> data = service.findByDate(regDay);

        return ApiResponse.success(data, data.size());

    }

    /**
     * 품목명 검색
     *
     * API
     * GET /api/prices/search?itemName=배추
     */
    @GetMapping("/search")
    public ApiResponse<List<PriceData>> searchPrices(@RequestParam String itemName) {

        List<PriceData> data = service.searchByItemName(itemName);

        return ApiResponse.success(data, data.size());

    }

    /**
     * 최근 데이터 조회
     *
     * API
     * GET /api/prices/latest?limit=50
     */
    @GetMapping("/latest")
    public ApiResponse<List<PriceData>> latest(@RequestParam(defaultValue = "50") int limit) {

        List<PriceData> data = service.findLatest(limit);

        return ApiResponse.success(data, data.size());

    }


}
