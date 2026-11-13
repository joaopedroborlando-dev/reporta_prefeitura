package com.portal.api.dto;

public record TokenResponse(String accessToken, String tokenType, long expiresIn) {
}
