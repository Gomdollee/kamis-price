package com.kamis.price.batch.kamis.reader;

import com.kamis.price.batch.kamis.dto.ExpandedPriceRow;
import com.kamis.price.batch.kamis.enums.CountryCode;
import com.kamis.price.external.kamis.dto.KamisItem;
import com.kamis.price.external.kamis.service.KamisApiService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * KAMIS API 데이터를 읽어오는 Reader
 *
 * 역할
 *  1.KAMIS API 호출
 *  2.응답 데이터를 DB row 단위로 변환
 *  3.하나씩 반환
 */
@Component
@StepScope
@RequiredArgsConstructor
public class KamisItemReader implements ItemReader<ExpandedPriceRow> {

    private final KamisApiService kamisApiService;

    /**
     * JobParameter 주입
     */
    @Value("#{jobParameters['regDay']}")
    private String regDay;

    @Value("#{jobParameters['itemCategoryCode']}")
    private String itemCategoryCode;

    /**
     * Reader 내부 버퍼
     */
    private List<ExpandedPriceRow> rows;

    private int index = 0;

    /**
     * Spring Batch에서 read()가 반복 호출됨
     */
    @Override
    public ExpandedPriceRow read() {

        if (rows == null) {
            rows = loadRows();
        }

        if (index >= rows.size()) {
            return null;
        }

        return rows.get(index++);
    }

    /**
     * API 호출 후 ExpandedPriceRow 생성
     */
    private List<ExpandedPriceRow> loadRows() {

        List<ExpandedPriceRow> result = new ArrayList<>();

        if (regDay == null || regDay.isBlank()) {
            regDay = LocalDate.now().toString();
        }

        List<String> productClsCodes = List.of("02", "01");

        for (String productCls : productClsCodes) {

            for (CountryCode countryCode : CountryCode.values()) {

                List<KamisItem> items =
                        kamisApiService.fetchCategoryPrices(
                                productCls,
                                itemCategoryCode,
                                countryCode.getCode(),
                                regDay
                        );

                for (KamisItem item : items) {

                    result.addAll(
                            expand(item, countryCode)
                    );
                }
            }
        }

        return result;
    }

    /**
     * KAMIS item → 여러 row로 확장
     */
    private List<ExpandedPriceRow> expand(KamisItem item, CountryCode countryCode) {

        List<ExpandedPriceRow> result = new ArrayList<>();

        add(result, item, "당일", item.getDpr1(), countryCode);
        add(result, item, "1일전", item.getDpr2(), countryCode);
        add(result, item, "1주일전", item.getDpr3(), countryCode);
        add(result, item, "2주일전", item.getDpr4(), countryCode);
        add(result, item, "1개월전", item.getDpr5(), countryCode);
        add(result, item, "1년전", item.getDpr6(), countryCode);
        add(result, item, "평년", item.getDpr7(), countryCode);

        return result;
    }

    /**
     * Row 생성
     */
    private void add(
            List<ExpandedPriceRow> list,
            KamisItem item,
            String type,
            String price,
            CountryCode countryCode
    ) {

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




