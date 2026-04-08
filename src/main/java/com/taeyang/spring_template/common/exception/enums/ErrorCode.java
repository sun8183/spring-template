package com.taeyang.spring_template.common.exception.enums;

import com.taeyang.spring_template.common.base.BaseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode implements BaseCode {

    // 공통
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "E001", "입력값이 올바르지 않습니다."),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E999", "서버 오류입니다."),

    // 로그인 실패 (HttpStatus.UNAUTHORIZED)
    LOGIN_NOT_MATCH(HttpStatus.UNAUTHORIZED, "A001", "아이디 또는 비밀번호가 올바르지 않습니다."),
    LOGIN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "A002", "아이디가 존재하지 않습니다."),

    // JWT 로그인 인증 실패 (HttpStatus.UNAUTHORIZED)
    JWT_INVALID(HttpStatus.UNAUTHORIZED, "J001", "유효하지 않은 액세스 토큰입니다."),
    JWT_EXPIRED(HttpStatus.UNAUTHORIZED, "J002", "액세스 토큰이 만료되었습니다."),
    JWT_EMPTY(HttpStatus.UNAUTHORIZED, "J003", "액세스 토큰이 존재하지 않습니다."),

    // 리프레시 토큰 발급 실패
    RT_INVALID(HttpStatus.UNAUTHORIZED, "R001", "유효하지 않은 리프레시 토큰입니다."),
    RT_EXPIRED(HttpStatus.UNAUTHORIZED, "R002", "리프레시 토큰이 만료되었습니다."),
    RT_EMPTY(HttpStatus.UNAUTHORIZED, "R003", "리프레시 토큰이 존재하지 않습니다."),
    RT_NOT_MATCH(HttpStatus.UNAUTHORIZED, "R004", "리프레시 토큰 정보가 일치하지 않습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
