package com.github.davidsmith.techtest.exception;

import com.github.davidsmith.techtest.exception.base.BadRequestException;

public class BadMachineException extends BadRequestException {
    public BadMachineException(String message, String error) {
        super(message, error);
    }
}
