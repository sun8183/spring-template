package com.taeyang.spring_template.common.exception.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.taeyang.spring_template.common.exception.enums.SuccessCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SuccessResponse<T>(
        String code,
        String message,
        T data
) {

    public static <T> SuccessResponse<T> of(T data) {
        return new SuccessResponse<>(SuccessCode.OK.code(), SuccessCode.OK.message(), data);
    }

    public static SuccessResponse<Void> noContent() {
        return new SuccessResponse<>(SuccessCode.OK.code(), SuccessCode.OK.message(), null);
    }
}
