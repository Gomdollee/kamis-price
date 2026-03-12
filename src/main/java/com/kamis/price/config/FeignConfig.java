package com.kamis.price.config;

import com.fasterxml.jackson.databind.ObjectMapper;
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

@Configuration
public class FeignConfig {

    @Bean
    public Decoder feignDecoder() {

        ObjectMapper objectMapper = new ObjectMapper();

        MappingJackson2HttpMessageConverter converter =
                new MappingJackson2HttpMessageConverter(objectMapper);

        // text/plain -> JSON 처리
        converter.setSupportedMediaTypes(
                List.of(
                        MediaType.APPLICATION_JSON,
                        MediaType.TEXT_PLAIN
                )
        );

        HttpMessageConverters converters =
                new HttpMessageConverters(converter);

        return new ResponseEntityDecoder(new SpringDecoder(() -> converters));
    }
}
