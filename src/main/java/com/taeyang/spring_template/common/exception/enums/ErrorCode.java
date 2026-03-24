package com.taeyang.spring_template.common.exception.enums;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // 공통
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "E001", "입력값이 올바르지 않습니다."),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E999", "서버 오류입니다."),

    // 로그인 실패 (HttpStatus.UNAUTHORIZED)
    LOGIN_NOT_MATCH(HttpStatus.UNAUTHORIZED, "A001", "아이디 또는 비밀번호가 올바르지 않습니다."),
    LOGIN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "A002", "아이디가 존재하지 않습니다."),

    // JWT 인증 실패 (HttpStatus.UNAUTHORIZED)
    JWT_INVALID(HttpStatus.UNAUTHORIZED, "J001", "유효하지 않은 토큰입니다."),
    JWT_EXPIRED(HttpStatus.UNAUTHORIZED, "J002", "토큰이 만료되었습니다."),
    JWT_MALFORMED(HttpStatus.UNAUTHORIZED, "J003", "잘못된 형식의 토큰입니다."),
    JWT_UNSUPPORTED(HttpStatus.UNAUTHORIZED, "J004", "지원하지 않는 토큰입니다."),
    JWT_SIGNATURE_INVALID(HttpStatus.UNAUTHORIZED, "J005", "토큰 서명이 유효하지 않습니다."),
    JWT_EMPTY(HttpStatus.UNAUTHORIZED, "J006", "토큰이 존재하지 않습니다."),

    // 리프레시 토큰 발급 실패
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "R001", "리프레시 토큰이 유효하지 않습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public HttpStatus status() { return status; }
    public String code() { return code; }
    public String message() { return message; }
}
