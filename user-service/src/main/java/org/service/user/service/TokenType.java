package org.service.user.service;

public enum TokenType {
    JWT("JWT");

    private String value;

    TokenType(String value) {
        this.value = value;
    }
}
