package com.kamis.price.external.kamis;

import com.kamis.price.external.kamis.dto.KamisResponseDto;
import com.kamis.price.external.kamis.service.KamisApiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class KamisApiServiceTest {

    @Autowired
    private KamisApiService kamisApiService;

    @Test
    void Kamis_API_호출_테스트() {

        KamisResponseDto response =
                kamisApiService.fetchCategoryPrices(
                        "02",
                        "200",
                        "1101",
                        "2024-01-01"
                );

        assertNotNull(response);
        assertNotNull(response.getData());
        assertEquals("000", response.getData().getErrorCode());
        assertFalse(response.getData().getItem().isEmpty());

        System.out.println(response.getData().getItem().get(0).getItemName());
    }
}
