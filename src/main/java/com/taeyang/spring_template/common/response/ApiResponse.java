package com.taeyang.spring_template.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.taeyang.spring_template.common.base.BaseCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        String code,
        String message,
        T data
) {
    // 1. 성공 응답 (데이터 포함)
    public static <T> ApiResponse<T> success(BaseCode baseCode, T data) {
        return new ApiResponse<>(baseCode.getCode(), baseCode.getMessage(), data);
    }

    // 2. 성공 응답 (데이터 없음)
    public static <T> ApiResponse<T> success(BaseCode baseCode) {
        return new ApiResponse<>(baseCode.getCode(), baseCode.getMessage(), null);
    }

    // 3. 실패 응답 (데이터 없음)
    public static <T> ApiResponse<T> error(BaseCode baseCode) {
        return new ApiResponse<>(baseCode.getCode(), baseCode.getMessage(), null);
    }

    // 4. 실패 응답 (데이터 포함 - 유효성 검사 에러 목록 등 전달용)
    public static <T> ApiResponse<T> error(BaseCode baseCode, T data) {
        return new ApiResponse<>(baseCode.getCode(), baseCode.getMessage(), data);
    }

    // 5. 실패 응답 (메시지만 커스텀하고 싶을 때)
    public static <T> ApiResponse<T> error(BaseCode baseCode, String message) {
        return new ApiResponse<>(baseCode.getCode(), message, null);
    }
}