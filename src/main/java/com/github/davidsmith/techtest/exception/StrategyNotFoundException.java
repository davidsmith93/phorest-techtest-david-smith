package com.github.davidsmith.techtest.exception;

import com.github.davidsmith.techtest.exception.base.InternalServerErrorException;

public class StrategyNotFoundException extends InternalServerErrorException {
    public StrategyNotFoundException(String message) {
        super(message);
    }
}
