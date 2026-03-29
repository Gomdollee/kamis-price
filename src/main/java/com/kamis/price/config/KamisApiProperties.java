package com.kamis.price.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * KAMIS API 설정 클래스
 *
 * application.yml 의
 *
 * kamis.api.*
 *
 * 설정을 자동으로 매핑한다.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "kamis.api")
public class KamisApiProperties {

    private String baseUrl; // KAMIS API
    private String certKey; // 인증 Key
    private String certId; // 인증 ID
    private boolean mockMode;  // Mock 모드 여부

}
