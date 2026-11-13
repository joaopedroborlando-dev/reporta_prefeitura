package com.portal.api.dto;

import com.portal.api.exception.InvalidRequestException;

public record RegisterRequest(String username, String email, String password) {

    private static final int MIN_PASSWORD_LENGTH = 8;

    public RegisterRequest {
        if (username == null || username.isBlank()) {
            throw new InvalidRequestException("username é obrigatório");
        }
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new InvalidRequestException("email inválido");
        }
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            throw new InvalidRequestException("password deve ter ao menos " + MIN_PASSWORD_LENGTH + " caracteres");
        }
        username = username.trim();
        email = email.trim().toLowerCase();
    }
}
