package com.kamis.price.batch.kamis.raw.partition;

import com.kamis.price.global.enums.CategoryCode;
import com.kamis.price.global.enums.CountryCode;
import com.kamis.price.global.enums.ProductClsCode;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class KamisPartitioner implements Partitioner {

    /**
     * 전체 수집 조건을 partition 단위를 분리
     *
     * 1 partition = productClsCode 1개 + categoryCode 1개 + countryCode 1개
     */
    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {

        Map<String, ExecutionContext> partitions = new HashMap<>();
        int index = 0;


        for (ProductClsCode productCls : ProductClsCode.values()) {
            for (CategoryCode category : CategoryCode.values()) {
                for (CountryCode country : CountryCode.values()) {

                    // worker step이 현재 어떤 조건을 처리 중인지 알 수 있도록 context에 주입
                    ExecutionContext context = new ExecutionContext();
                    context.putString("productClsCode", productCls.getCode());
                    context.putString("categoryCode", category.getCode());
                    context.putString("countryCode", country.getCode());

                    partitions.put("partition" + index, context);
                    index++;
                }
            }
        }

        return partitions;
    }
}
