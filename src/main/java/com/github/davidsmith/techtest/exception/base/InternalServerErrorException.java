package com.github.davidsmith.techtest.exception.base;

import lombok.Getter;

@Getter
public abstract class InternalServerErrorException extends RuntimeException {
    public InternalServerErrorException(String message) {
        super(message);
    }
}
