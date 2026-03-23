package com.kamis.price.batch.kamis.price.processor;

import com.kamis.price.domain.price.entity.PriceData;
import com.kamis.price.domain.raw.entity.KamisRawItem;
import com.kamis.price.external.kamis.dto.KamisItemDto;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Component
public class KamisProcessor implements ItemProcessor<KamisRawItem, List<PriceData>> {


    /**
     * Raw 데이터 1건 → 여러 PriceData 생성
     */
    @Override
    public List<PriceData> process(KamisRawItem item) {

        List<PriceData> result = new ArrayList<>();

        // dpr1 ~ dpr7 각각 row 생성
        add(result, item, item.getDay1(), item.getDpr1());
        add(result, item, item.getDay2(), item.getDpr2());
        add(result, item, item.getDay3(), item.getDpr3());
        add(result, item, item.getDay4(), item.getDpr4());
        add(result, item, item.getDay5(), item.getDpr5());
        add(result, item, item.getDay6(), item.getDpr6());
        add(result, item, item.getDay7(), item.getDpr7());

        return result.isEmpty() ? null : result;
    }

    /**
     * price row 생성 helper
     */
    private void add(
            List<PriceData> list,
            KamisRawItem item,
            String priceType,
            String priceStr
    ) {

        // 값 없는 경우 skip
        if (priceStr == null || priceStr.isBlank() || "-".equals(priceStr)) {
            return;
        }

        int price;

        try {
            // "5,500" → 5500 변환
            price = Integer.parseInt(priceStr.replace(",", ""));
        } catch (Exception e) {
            return;
        }

        // 0 이하 값 skip
        if (price <= 0) {
            return;
        }

        // PriceData 생성
        list.add(
                PriceData.builder()
                        .sourceType("CATEGORY")
                        .itemCode(item.getItemCode())
                        .itemName(item.getItemName())
                        .kindCode(item.getKindCode())
                        .kindName(item.getKindName())
                        .rank(item.getRank())
                        .countryCode(item.getCountryCode())
                        .unit(item.getUnit())
                        .price(price)
                        .priceType(priceType)
                        .regDay((item.getRegday()))
                        .build()
        );
    }
}
