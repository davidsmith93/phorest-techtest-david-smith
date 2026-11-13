package com.github.davidsmith.techtest.controller;

import com.github.davidsmith.techtest.controller.model.ErrorResponse;
import com.github.davidsmith.techtest.exception.base.BadRequestException;
import com.github.davidsmith.techtest.exception.base.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlers {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleValidationError(MethodArgumentNotValidException ex) {
        final Collection<ErrorResponse.Error> errors = ex.getFieldErrors().stream()
                .map(fieldError -> new ErrorResponse.Error(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();

        final ErrorResponse errorResponse = new ErrorResponse(
                "error.validation",
                "Validation failed for request",
                errors
        );

        log.warn("Handling MethodArgumentNotValidException", ex);

        return errorResponse;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ErrorResponse handleBadRequestException(BadRequestException ex) {
        final Collection<ErrorResponse.Error> errors = List.of(
                new ErrorResponse.Error("bad_request", ex.getError())
        );

        final ErrorResponse errorResponse = new ErrorResponse(
                "error.bad_request",
                ex.getMessage(),
                errors
        );

        log.warn("Handling BadRequestException", ex);

        return errorResponse;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorResponse handleNotFoundException(NotFoundException ex) {
        final Collection<ErrorResponse.Error> errors = List.of(
            new ErrorResponse.Error("not_found", ex.getError())
        );

        final ErrorResponse errorResponse = new ErrorResponse(
            "error.not_found",
            ex.getMessage(),
            errors
        );

        log.warn("Handling NotFoundException", ex);

        return errorResponse;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErrorResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        final Collection<ErrorResponse.Error> errors = List.of(
                new ErrorResponse.Error("bad_request", ex.getMostSpecificCause().getMessage())
        );

        final ErrorResponse errorResponse = new ErrorResponse(
                "error.bad_request",
                "Unable to handle request",
                errors
        );

        log.warn("Handling HttpMessageNotReadableException", ex);

        return errorResponse;
    }
}
