package com.github.davidsmith.techtest.exception;

import com.github.davidsmith.techtest.exception.base.NotFoundException;

public class EmptyMachineException extends NotFoundException {
    public EmptyMachineException(String message, String error) {
        super(message, error);
    }
}
