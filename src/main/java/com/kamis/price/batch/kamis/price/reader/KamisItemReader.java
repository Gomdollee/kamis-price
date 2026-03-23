package com.kamis.price.batch.kamis.price.reader;

import com.kamis.price.domain.raw.entity.KamisRawItem;
import com.kamis.price.domain.raw.repository.KamisRawItemRepository;
import com.kamis.price.external.kamis.dto.KamisItemDto;
import com.kamis.price.external.kamis.dto.KamisResponseDto;
import com.kamis.price.external.kamis.service.KamisApiService;
import com.kamis.price.global.enums.CategoryCode;
import com.kamis.price.global.enums.CountryCode;
import com.kamis.price.global.enums.ProductClsCode;
import com.kamis.price.global.enums.RawStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * KAMIS API 데이터를 읽어오는 Reader
 *
 * 역할
 *  1.KAMIS API 호출
 *  2.응답 데이터를 DB row 단위로 변환
 *  3.Spring Batch가 read()를 반복 호출하면서 데이터를 하나씩 가져감
 */
@Component
@StepScope
@RequiredArgsConstructor
@Slf4j
public class KamisItemReader implements ItemReader<KamisRawItem> {

    private final KamisRawItemRepository repository;

    private Iterator<KamisRawItem> iterator;

    @Override
    public KamisRawItem read() {

        if (iterator == null) {
            log.info("PROCESSED Raw 데이터 조회 시작");

            iterator = repository.findByProcessingStatus(RawStatus.PROCESSED).iterator();
        }

        return iterator.hasNext() ? iterator.next() : null;
    }
}




