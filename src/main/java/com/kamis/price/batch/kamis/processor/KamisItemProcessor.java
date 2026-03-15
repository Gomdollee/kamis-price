package com.kamis.price.batch.kamis.processor;

import com.kamis.price.batch.kamis.dto.ExpandedPriceRow;
import com.kamis.price.domain.price.entity.PriceData;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Batch Processor
 *
 * ExpandedPriceRow → PriceData Entity 변환
 *
 *  Reader가 읽어온 데이터를
 *  DB 저장 가능한 Entity로 변환
 */
@Component
public class KamisItemProcessor implements ItemProcessor<ExpandedPriceRow, PriceData> {

    @Override
    public PriceData process(ExpandedPriceRow item)  {

        PriceData entity = new PriceData();

        entity.setSourceType(item.getSourceType());
        entity.setItemCode(item.getItemCode());
        entity.setItemName(item.getItemName());

        entity.setKindCode(item.getKindCode());
        entity.setKindName(item.getKindName());

        entity.setRank(item.getRank());

        entity.setCountryCode(item.getCountryCode());
        entity.setCountryName(item.getCountryName());

        entity.setUnit(item.getUnit());

        entity.setPrice(parse(item.getPriceRaw()));
        entity.setPriceType(item.getPriceType());
        entity.setRegDay(LocalDate.parse(item.getRegDay()));

        return entity;
    }

    /**
     * 가격 문자열 → 숫자 변환
     */
    private Integer parse(String price) {

        if (price == null || price.isBlank()) {
            return null;
        }

        return Integer.parseInt(price.replace(",",""));
     }
}
