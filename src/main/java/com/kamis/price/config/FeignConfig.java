package com.kamis.price.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Logger;
import feign.codec.Decoder;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.List;

/**
 * OpenFeign 설정 클래스
 *
 * 역할
 * - 외부 API 응답(JSON)을 Java 객체로 변환
 */
@Configuration
public class FeignConfig {

    /**
     * Feign Decoder 설정
     *
     * 외부 API 응답을 Java 객체로 변환하는 역할
     *
     * 기본적으로 SpringDecoder를 사용하지만
     * text/plain 응답도 JSON으로 파싱 가능하도록 설정한다.
     */
    @Bean
    public Decoder feignDecoder(ObjectMapper objectMapper) {

        // Jackson 기반 JSON 변환기 생성
        MappingJackson2HttpMessageConverter converter =
                new MappingJackson2HttpMessageConverter(objectMapper);

        /**
         * 지원하는 MediaType 설정
         *
         * 일부 API는 Content-Type을 text/plain으로 반환하면서
         * 실제 내용은 JSON인 경우가 있음
         */
        converter.setSupportedMediaTypes(
                List.of(
                        MediaType.APPLICATION_JSON,
                        MediaType.TEXT_PLAIN
                )
        );

        // Spring HTTP Converter 등록
        HttpMessageConverters converters =
                new HttpMessageConverters(converter);

        /**
         * Feign Decoder 구성
         *
         * ResponseEntityDecoder
         *  -> SpringDecoder
         *      -> HttpMessageConverters
         */
        return new ResponseEntityDecoder(new SpringDecoder(() -> converters));
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
