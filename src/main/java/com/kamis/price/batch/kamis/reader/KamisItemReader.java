package com.kamis.price.batch.kamis.reader;

import com.kamis.price.batch.kamis.dto.ExpandedPriceRow;
import com.kamis.price.batch.kamis.enums.CountryCode;
import com.kamis.price.external.kamis.dto.KamisItem;
import com.kamis.price.external.kamis.service.KamisApiService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * KAMIS API를 호출해서 item 데이터를 가져온 뒤,
 * dpr1~dpr7을 DB 저장용 row 단위로 펼쳐서 하나씩 반환하는 Reader
 */
@Component
@RequiredArgsConstructor
public class KamisItemReader implements ItemReader<ExpandedPriceRow> {

    private final KamisApiService kamisApiService;

    private final List<ExpandedPriceRow> rows = new ArrayList<>();
    private int index = 0;

    @PostConstruct
    public void init() {

        String regDay = LocalDate.now().toString();

        List<String> productClsCodes = List.of("02", "01");
        List<String> categoryCodes = List.of("100", "200", "300", "400", "500", "600");

        for (String productCls : productClsCodes) {
            for (String category : categoryCodes) {
                for (CountryCode countryCode : CountryCode.values()) {

                    List<KamisItem> items =
                            kamisApiService.fetchCategoryPrices(productCls, category, countryCode.getCode() , regDay);

                    if (items.isEmpty()) {
                        continue;
                    }

                    for (KamisItem item : items) {
                        rows.addAll(expand(item, countryCode, regDay));
                    }
                }
            }
        }
    }

    @Override
    public ExpandedPriceRow read() {

        if(index >= rows.size()) {
            return null;
        }

        return rows.get(index++);
    }

    private List<ExpandedPriceRow> expand(KamisItem item, CountryCode countryCode , String regDay) {

        List<ExpandedPriceRow> result = new ArrayList<>();

        add(result, item, regDay, "당일", item.getDpr1(), countryCode);
        add(result, item, regDay, "1일전", item.getDpr2(),countryCode);
        add(result, item, regDay, "1주일전", item.getDpr3(), countryCode);
        add(result, item, regDay, "2주일전", item.getDpr4(), countryCode);
        add(result, item, regDay, "1개월전", item.getDpr5(), countryCode);
        add(result, item, regDay, "1년전", item.getDpr6(), countryCode);
        add(result, item, regDay, "평년", item.getDpr7(), countryCode);

        return result;

    }

    private void add(List<ExpandedPriceRow> list,
                     KamisItem item,
                     String regDay,
                     String type,
                     String price,
                     CountryCode countryCode) {

        if (price == null || price.isBlank() || "-".equals(price)) {
            return;
        }

        list.add(
                ExpandedPriceRow.builder()
                        .sourceType("CATEGORY")
                        .itemCode(item.getItem_code())
                        .itemName(item.getItem_name())
                        .kindCode(item.getKind_code())
                        .kindName(item.getKind_name())
                        .rank(item.getRank())
                        .countryCode(countryCode.getCode())
                        .countryName(countryCode.getName())
                        .unit(item.getUnit())
                        .priceRaw(price)
                        .priceType(type)
                        .regDay(regDay)
                        .build()
        );

    }

}
