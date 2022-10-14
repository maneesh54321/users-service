package org.service.user.jwt;

public enum TokenType {
    JWT("JWT");

    private String value;

    TokenType(String value) {
        this.value = value;
    }
}
