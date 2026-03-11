package com.kamis.price.domain.price.service;

import com.kamis.price.domain.price.entity.PriceData;
import com.kamis.price.domain.price.repository.PriceDataRepository;
import com.kamis.price.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * 가격 조회 관련 비즈니스 로직 담당 서비스
 */
@Service
@RequiredArgsConstructor
public class PriceQueryService {

    private final PriceDataRepository repository;

    /**
     * 날짜 기준 가격 조회
     *
     * API
     * GET /api/prices?regDay=yyyy-MM-dd
     */
    public List<PriceData> findByDate(String regDay) {

        LocalDate date = LocalDate.parse(regDay);

        List<PriceData> result = repository.findByRegDay(date);

        if (result.isEmpty()) {
            throw new CustomException("조회 결과가 없습니다.");
        }

        return result;
    }

    /**
     * 품목명 검색
     *
     * API
     * GET /api/prices/search?itemName=배추
     */
    public List<PriceData> searchByItemName(String itemName) {

        List<PriceData> result = repository.findByItemNameContaining(itemName);

        if (result.isEmpty()) {
            throw new CustomException("조회 결과가 없습니다.");
        }

        return result;
    }


    /**
     * 최근 데이터 조회
     *
     * API
     * GET /api/prices/latest?limit=50
     */
    public List<PriceData> findLatest(int limit) {

        Pageable pageable = PageRequest.of(0,limit);

        List<PriceData> result = repository.findAllByOrderByCreatedAtDesc(pageable)
                .getContent();

        if (result.isEmpty()) {
            throw new CustomException("조회 결과가 없습니다.");
        }

        return result;

    }

}
