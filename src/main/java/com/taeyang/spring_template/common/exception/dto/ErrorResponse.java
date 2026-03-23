package com.taeyang.spring_template.common.exception.dto;

import com.taeyang.spring_template.common.exception.enums.ErrorCode;

public record ErrorResponse(String code, String message, Object details) {

    // 추가 정보(Validation 결과 등)가 필요한 에러용
    public static ErrorResponse of(ErrorCode code, Object details) {
        return new ErrorResponse(code.code(), code.message(), details);
    }
}
