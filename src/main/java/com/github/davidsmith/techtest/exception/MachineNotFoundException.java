package com.github.davidsmith.techtest.exception;

import com.github.davidsmith.techtest.exception.base.NotFoundException;

public class MachineNotFoundException extends NotFoundException {
    public MachineNotFoundException(String message, String error) {
        super(message, error);
    }
}
