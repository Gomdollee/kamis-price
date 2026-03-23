package com.kamis.price.domain.raw.repository;

import com.kamis.price.domain.raw.entity.KamisRawItem;
import com.kamis.price.global.enums.RawStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface KamisRawItemRepository extends JpaRepository<KamisRawItem, Long> {

    boolean existsByItemCodeAndKindCodeAndRankCodeAndRegdayAndCountryCode(
            String itemCode,
            String kindCode,
            String rankCode,
            LocalDate regday,
            String countryCode
    );

    Optional<KamisRawItem> findByItemCodeAndKindCodeAndRankCodeAndRegdayAndCountryCode(
            String itemCode,
            String kindCode,
            String rankCode,
            LocalDate regday,
            String countryCode
    );

    @Modifying
    @Query(value = """
            INSERT INTO kamis_raw_item (
                request_id, country_code, regday, category_code,
                item_name, item_code, kind_name, kind_code,
                rank_name, rank_code, unit,
                day1, dpr1, day2, dpr2, day3, dpr3,
                day4, dpr4, day5, dpr5, day6, dpr6,
                day7, dpr7,
                processing_status, created_at, updated_at
            ) VALUES (
                :requestId, :countryCode, :regday, :categoryCode,
                :itemName, :itemCode, :kindName, :kindCode,
                :rank, :rankCode, :unit,
                :day1, :dpr1, :day2, :dpr2, :day3, :dpr3,
                :day4, :dpr4, :day5, :dpr5, :day6, :dpr6,
                :day7, :dpr7,
                'PROCESSED', NOW(), NOW()
            )
            ON DUPLICATE KEY UPDATE
                updated_at = NOW(),
                request_id = VALUES(request_id),
                item_name = VALUES(item_name),
                kind_name = VALUES(kind_name),
                rank_name = VALUES(rank_name),
                unit = VALUES(unit),
                dpr1 = VALUES(dpr1),
                dpr2 = VALUES(dpr2),
                dpr3 = VALUES(dpr3),
                dpr4 = VALUES(dpr4),
                dpr5 = VALUES(dpr5),
                dpr6 = VALUES(dpr6),
                dpr7 = VALUES(dpr7)
            """, nativeQuery = true)
    void upsert(
            @Param("requestId") Long requestId,
            @Param("countryCode") String countryCode,
            @Param("regday") LocalDate regday,
            @Param("categoryCode") String categoryCode,
            @Param("itemName") String itemName,
            @Param("itemCode") String itemCode,
            @Param("kindName") String kindName,
            @Param("kindCode") String kindCode,
            @Param("rank") String rank,
            @Param("rankCode") String rankCode,
            @Param("unit") String unit,
            @Param("day1") String day1,
            @Param("dpr1") String dpr1,
            @Param("day2") String day2,
            @Param("dpr2") String dpr2,
            @Param("day3") String day3,
            @Param("dpr3") String dpr3,
            @Param("day4") String day4,
            @Param("dpr4") String dpr4,
            @Param("day5") String day5,
            @Param("dpr5") String dpr5,
            @Param("day6") String day6,
            @Param("dpr6") String dpr6,
            @Param("day7") String day7,
            @Param("dpr7") String dpr7
    );

    List<KamisRawItem> findByProcessingStatus(RawStatus status);
}