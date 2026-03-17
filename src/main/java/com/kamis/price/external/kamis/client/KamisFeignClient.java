package com.kamis.price.external.kamis.client;

import com.kamis.price.config.FeignConfig;
import com.kamis.price.external.kamis.dto.KamisResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * FeignClient
 *
 * 외부 API 서버를 호출하는 인터페이스
 */

@FeignClient(
        name = "kamisClient",
        url = "${kamis.api.base-url}",
        configuration = FeignConfig.class
)
public interface KamisFeignClient {

    /**
     * 일별 부류별 도.소매가격정보
     */
    @GetMapping("/service/price/xml.do")
    KamisResponseDto getPriceList(

            @RequestParam("action") String action,
            @RequestParam("p_cert_key") String certKey,                     // 인증키
            @RequestParam("p_cert_id") String certId,                       // 인증ID
            @RequestParam("p_returntype") String returnType,                // 응답타입 (json)
            @RequestParam("p_product_cls_code") String productClsCode,      // 도매/소매 구분
            @RequestParam("p_item_category_code") String categoryCode,      // 품목 카테고리
            @RequestParam("p_country_code") String countryCode,             // 지역코드
            @RequestParam("p_regday") String regday,                        // 조회 날짜
            @RequestParam("p_convert_kg_yn") String convertKgYn             // kg 변환 여부

    );

}
