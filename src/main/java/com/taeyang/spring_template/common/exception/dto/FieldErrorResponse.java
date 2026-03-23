package com.taeyang.spring_template.common.exception.dto;

import org.springframework.validation.FieldError;

public record FieldErrorResponse(String field, String message) {
    public FieldErrorResponse(FieldError error) {
        this(error.getField(), error.getDefaultMessage());
    }
}