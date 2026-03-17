package com.kamis.price.domain.raw.repository;

import com.kamis.price.domain.raw.entity.KamisRawItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KamisRawItemRepository extends JpaRepository<KamisRawItem, Long> {
}
