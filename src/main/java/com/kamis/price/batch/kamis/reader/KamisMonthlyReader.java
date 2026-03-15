package com.kamis.price.batch.kamis.reader;

import com.kamis.price.batch.kamis.dto.ExpandedPriceRow;
import com.kamis.price.external.kamis.dto.KamisMonthlyItem;
import com.kamis.price.external.kamis.service.KamisApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@StepScope
@RequiredArgsConstructor
public class KamisMonthlyReader implements ItemReader<ExpandedPriceRow> {

    private final KamisApiService kamisApiService;


    @Value("#{jobParameters['yyyy']}")
    private String yyyy;

    @Value("#{jobParameters['itemCategoryCode']}")
    private String itemCategoryCode;

    private List<ExpandedPriceRow> rows;
    private int index = 0;

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

    private List<ExpandedPriceRow> loadRows() {

        List<KamisMonthlyItem> items =
                kamisApiService.fetchMonthlyPrices(
                        itemCategoryCode,
                        yyyy
                );

        List<ExpandedPriceRow> result = new ArrayList<>();

        for (KamisMonthlyItem item : items) {

            expandMonth(result, item);

        }

        return result;
    }
    /**
     * 월별 데이터(m1~m12)를 row 형태로 확장
     */
    private void expandMonth(
            List<ExpandedPriceRow> list,
            KamisMonthlyItem item
    ){

        add(list,item,"01",item.getM1());
        add(list,item,"02",item.getM2());
        add(list,item,"03",item.getM3());
        add(list,item,"04",item.getM4());
        add(list,item,"05",item.getM5());
        add(list,item,"06",item.getM6());
        add(list,item,"07",item.getM7());
        add(list,item,"08",item.getM8());
        add(list,item,"09",item.getM9());
        add(list,item,"10",item.getM10());
        add(list,item,"11",item.getM11());
        add(list,item,"12",item.getM12());
    }

    /**
     * 개별 row 생성
     */
    private void add(
            List<ExpandedPriceRow> list,
            KamisMonthlyItem item,
            String month,
            String price
    ){

        if(price == null || price.isBlank()) return;

        String regDay = item.getYyyy() + "-" + month + "-01";

        list.add(
                ExpandedPriceRow.builder()
                        .sourceType("MONTHLY")
                        .priceRaw(price)
                        .priceType("MONTH")
                        .regDay(regDay)
                        .build()
        );
    }


}
