package com.taeyang.spring_template.common.exception.enums;

public enum SuccessCode {

    OK("S001", "요청 성공");

    private final String code;
    private final String message;

    SuccessCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String code() { return code; }
    public String message() { return message; }
}