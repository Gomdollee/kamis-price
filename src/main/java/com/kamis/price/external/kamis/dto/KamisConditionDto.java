package com.kamis.price.external.kamis.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

/**
 * 요청 정보 DTO
 */


@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KamisConditionDto {

    /**
     * 인증Key
     */
    private String p_cert_key;

    /**
     *	요청자id
     */
    private String p_cert_id;

    /**
     *	Return Type (json:Json 데이터 형식, xml:XML데이터형식)
     */
    private String p_returntype;

    /**
     * 구분 ( 01:소매, 02:도매, default:02 )
     */
    private String p_product_cls_code;

    /**
     * 부류코드(100:식량작물, 200:채소류, 300:특용작물, 400:과일류, 500:축산물, 600:수산물, default:100)
     */
    private String p_item_category_code;
    /**
     * 소매가격 선택가능 지역
     * (1101:서울, 2100:부산, 2200:대구, 2300:인천, 2401:광주, 2501:대전, 2601:울산, 3111:수원
     * 3214:강릉, 3211:춘천, 3311:청주, 3511:전주, 3711:포항, 3911:제주, 3113:의정부, 3613:순천, 3714:안동, 3814:창원,
     * 3145:용인, 2701:세종, 3112:성남, 3138:고양, 3411:천안, 3818:김해)
     * 도매가격 선택가능 지역 (1101:서울, 2100:부산, 2200:대구, 2401:광주, 2501:대전)
     * default : 전체지역
     */
    private String p_country_code;

    /**
     * 날짜 : yyyy-mm-dd (default : 최근 조사일자)
     */
    private String p_regday;

    /**
     * 	kg단위 환산여부(Y : 1kg 단위표시, N : 정보조사 단위표시, ex: 쌀 20kg)
     * 	default : N
     */
    private String p_convert_kg_yn;



}
