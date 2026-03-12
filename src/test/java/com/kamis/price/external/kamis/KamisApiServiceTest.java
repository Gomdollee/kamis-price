package com.kamis.price.external.kamis;

import com.kamis.price.external.kamis.dto.KamisItemDto;
import com.kamis.price.external.kamis.service.KamisApiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class KamisApiServiceTest {

    @Autowired
    KamisApiService kamisApiService;

    /**
     * 외부 KAMIS API 실제 호출 테스트
     */
    @Test
    void kamis_api_connection_test() {

        List<KamisItemDto> items = kamisApiService.fetchPrices();

        // 데이터 개수 확인
        System.out.println("item size = " +items.size());

        // 데이터 출력
        items.stream()
                .limit(5)
                .forEach(item -> {
                    System.out.println(
                            item.getItem_name() + "/" + item.getDpr1()
                    );
                });

        assertFalse(items.isEmpty());
    }
}
