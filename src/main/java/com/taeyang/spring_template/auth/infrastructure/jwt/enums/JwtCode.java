package com.taeyang.spring_template.auth.infrastructure.jwt.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtCode {

    VALID("토큰이 유효합니다."),
    INVALID("유효하지 않은 토큰입니다."),
    EXPIRED("만료된 토큰입니다."),
    EMPTY("토큰이 비어있거나 전달되지 않았습니다.");

    private final String message;
}