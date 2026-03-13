package com.kamis.price.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * application.yml 의 kamis.api 설정을 매핑
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "kamis.api")
public class KamisApiProperties {

    private String baseUrl; // KAMIS API
    private String certKey; // 인증 Key
    private String certId; // 인증 ID
    private boolean mockMode;  // Mock 모드 여부

    // API 설정이 완료되었는지 체크
    public boolean isConfigured() {
        return certKey != null && certId != null;
    }
}
