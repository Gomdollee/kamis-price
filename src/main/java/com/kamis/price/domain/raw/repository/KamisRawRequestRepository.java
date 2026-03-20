package com.kamis.price.domain.raw.repository;

import com.kamis.price.domain.raw.entity.KamisRawRequest;
import org.springframework.data.jpa.repository.JpaRepository;


public interface KamisRawRequestRepository extends JpaRepository<KamisRawRequest , Long> {

}
