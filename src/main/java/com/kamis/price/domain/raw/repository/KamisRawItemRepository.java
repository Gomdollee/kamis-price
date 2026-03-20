package com.kamis.price.domain.raw.repository;

import com.kamis.price.domain.raw.entity.KamisRawItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
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
}
