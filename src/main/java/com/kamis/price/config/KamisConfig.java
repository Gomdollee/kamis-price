package com.kamis.price.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(KamisApiProperties.class)
public class KamisConfig {
}
