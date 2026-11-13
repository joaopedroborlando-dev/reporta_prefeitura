package com.portal.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "security.jwt")
public record JwtProperties(String issuer, Duration expiration) {

    public JwtProperties {
        if (issuer == null || issuer.isBlank()) {
            issuer = "portal-api";
        }
        if (expiration == null) {
            expiration = Duration.ofHours(1);
        }
    }
}
