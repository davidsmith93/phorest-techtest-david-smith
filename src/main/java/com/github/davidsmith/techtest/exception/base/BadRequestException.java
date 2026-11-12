package com.github.davidsmith.techtest.exception.base;

import lombok.Getter;

@Getter
public abstract class BadRequestException extends RuntimeException {
    private final String error;

    public BadRequestException(String message, String error) {
        super(message);
        this.error = error;
    }
}
