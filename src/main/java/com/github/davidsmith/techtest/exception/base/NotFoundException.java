package com.github.davidsmith.techtest.exception.base;

import lombok.Getter;

@Getter
public abstract class NotFoundException extends RuntimeException {
    private final String error;

    public NotFoundException(String message, String error) {
        super(message);
        this.error = error;
    }
}
