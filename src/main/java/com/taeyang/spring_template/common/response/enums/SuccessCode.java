package com.taeyang.spring_template.common.response.enums;

import com.taeyang.spring_template.common.base.BaseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessCode implements BaseCode {
    OK(HttpStatus.OK, "S001", "요청 성공");

    private final HttpStatus status;
    private final String code;
    private final String message;
}