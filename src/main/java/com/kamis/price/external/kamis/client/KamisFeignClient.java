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
        url = "${kamis.api.url}",
        configuration = FeignConfig.class
)
public interface KamisFeignClient {

    @GetMapping("/service/price/xml.do")
    KamisResponseDto getPriceList(

            @RequestParam("action") String action,

            // 인증키
            @RequestParam("p_cert_key") String certKey,

            // 인증ID
            @RequestParam("p_cert_id") String certId,

            // 응답타입 (json)
            @RequestParam("p_returntype") String returnType,

            // 도매/소매 구분
            @RequestParam("p_product_cls_code") String productClsCode,

            // 품목 카테고리
            @RequestParam("p_item_category_code") String categoryCode,

            // 지역코드
            @RequestParam("p_country_code") String countryCode,

            // 조회 날짜
            @RequestParam("p_regday") String regday,

            // kg 변환 여부
            @RequestParam("p_convert_kg_yn") String convertKgYn

    );
}
