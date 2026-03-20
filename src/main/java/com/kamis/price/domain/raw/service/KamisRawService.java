package com.kamis.price.domain.raw.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kamis.price.domain.raw.entity.KamisRawItem;
import com.kamis.price.domain.raw.entity.KamisRawRequest;
import com.kamis.price.domain.raw.repository.KamisRawItemRepository;
import com.kamis.price.domain.raw.repository.KamisRawRequestRepository;
import com.kamis.price.external.kamis.dto.KamisItemDto;
import com.kamis.price.external.kamis.dto.KamisRawCollectCommand;
import com.kamis.price.external.kamis.dto.KamisResponseDto;
import com.kamis.price.external.kamis.service.KamisApiService;
import com.kamis.price.global.enums.CategoryCode;
import com.kamis.price.global.enums.CountryCode;
import com.kamis.price.global.enums.ProductClsCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KamisRawService {

    private final KamisApiService kamisApiService;
    private final KamisRawRequestRepository kamisRawRequestRepository;
    private final KamisRawItemRepository kamisRawItemRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public Long collectAndSave(KamisRawCollectCommand command) {
        KamisRawRequest request= KamisRawRequest.create(
                command.getProductClsCode(),
                command.getCategoryCode(),
                command.getCountryCode(),
                command.getRegday(),
                kamisApiService.getCertKey(),
                kamisApiService.getCertId()
        );

        kamisRawRequestRepository.save(request);

        try {
            KamisResponseDto response = kamisApiService.fetchCategoryPrices(
                    command.getProductClsCode(),
                    command.getCategoryCode(),
                    command.getCountryCode(),
                    command.getRegday().toString()
            );

            String rawJson = toJson(response);
            String errorCode = extractErrorCode(response);
            request.complete(errorCode, rawJson);
            List<KamisItemDto> items = extractItems(response);

            for (KamisItemDto item : items) {
                saveItemIfNotExists(
                        request,
                        item,
                        command.getCategoryCode(),
                        command.getCountryCode(),
                        command.getRegday()
                );
            }

            log.info("KAMIS raw 저장 완료 - requestId={}, itemCount={}", request.getId(), items.size());
            return request.getId();

        } catch (Exception e) {
            log.error("KAMIS raw 저장 실패 - productClsCode={}, categoryCode={}, countryCode={}, regday={}",
                    command.getProductClsCode(),
                    command.getCategoryCode(),
                    command.getCountryCode(),
                    command.getRegday(),
                    e
            );
            request.markFailed(e.getMessage());
            throw e;
        }
    }

    private void saveItemIfNotExists(
            KamisRawRequest request,
            KamisItemDto item,
            String categoryCode,
            String countryCode,
            LocalDate regday
    ) {
        boolean exists = kamisRawItemRepository.existsByItemCodeAndKindCodeAndRankCodeAndRegdayAndCountryCode(
                item.getItemCode(),
                item.getKindCode(),
                item.getRankCode(),
                regday,
                countryCode
        );

        if (exists) {
            log.debug("중복 raw item 스킵 - itemCode={}, kindCode={}, rankCode={}, regday={}, countryCode={}",
                    item.getItemCode(),
                    item.getKindCode(),
                    item.getRankCode(),
                    regday,
                    countryCode
            );
            return;
        }

        KamisRawItem entity = KamisRawItem.from(
                request,
                item,
                categoryCode,
                countryCode,
                regday
        );

        entity.markProcessed();
        kamisRawItemRepository.save(entity);
    }

    private List<KamisItemDto> extractItems(KamisResponseDto response) {
        if (response == null || response.getData() == null || response.getData().getItem() == null) {
            return Collections.emptyList();
        }
        return response.getData().getItem();
    }

    private String toJson(KamisResponseDto response) {
        try {
            return objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("KAMIS 응답 JSON 직렬화에 실패했습니다.", e);
        }
    }

    private String extractErrorCode(KamisResponseDto response) {
        if (response == null || response.getData() == null) {
            return null;
        }
        return response.getData().getErrorCode();
    }



}
