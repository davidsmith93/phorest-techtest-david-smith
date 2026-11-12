package com.github.davidsmith.techtest.controller.model;

import java.util.Collection;

public record ErrorResponse(String code, String message, Collection<Error> errors) {
    public record Error(String field, String error) {

    }
}
