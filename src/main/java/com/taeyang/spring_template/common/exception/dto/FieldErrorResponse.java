package com.taeyang.spring_template.common.exception.dto;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.stream.Collectors;

public record FieldErrorResponse(
        String field,
        String value,
        String reason
) {
    /**
     * BindingResult로부터 FieldErrorResponse 리스트를 생성합니다.
     * GlobalExceptionHandler에서 사용하기 편리하도록 정적 팩토리 메서드를 추가했습니다.
     */
    public static List<FieldErrorResponse> of(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .map(error -> new FieldErrorResponse(
                        error.getField(),
                        error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                        error.getDefaultMessage()))
                .collect(Collectors.toList());
    }
}