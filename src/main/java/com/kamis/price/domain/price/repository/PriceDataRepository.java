package com.kamis.price.domain.price.repository;

import com.kamis.price.domain.price.entity.PriceData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PriceDataRepository extends JpaRepository<PriceData, Long> {

    /**
     * 날짜 기준 가격 조회
     *
     * GET /api/prices?regDay=
     */
    List<PriceData> findByRegDay(LocalDate regDay);

    /**
     * 품목명 검색
     *
     * GET /api/prices/search
     */
    List<PriceData> findByItemNameContaining(String itemName);

    /**
     * 최근 데이터 조회
     *
     * GET /api/prices/latest
     */
    Page<PriceData> findAllByOrderByCreatedAtDesc(Pageable pageable);

}
