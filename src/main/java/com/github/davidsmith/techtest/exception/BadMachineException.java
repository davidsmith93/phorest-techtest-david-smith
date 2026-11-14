package com.github.davidsmith.techtest.exception;

import com.github.davidsmith.techtest.exception.base.InternalServerErrorException;

public class BadMachineException extends InternalServerErrorException {
    public BadMachineException(String message) {
        super(message);
    }
}
