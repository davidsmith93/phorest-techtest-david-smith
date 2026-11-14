package com.github.davidsmith.techtest.exception;

import com.github.davidsmith.techtest.exception.base.BadRequestException;

public class ValidationException extends BadRequestException {
    public ValidationException(String message, String error) {
        super(message, error);
    }
}
