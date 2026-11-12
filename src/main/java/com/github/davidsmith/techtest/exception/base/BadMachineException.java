package com.github.davidsmith.techtest.exception.base;

public class BadMachineException extends BadRequestException {
    public BadMachineException(String message, String error) {
        super(message, error);
    }
}
