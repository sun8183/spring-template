package com.taeyang.spring_template.common.exception;

import com.taeyang.spring_template.common.exception.enums.ErrorCode;
import lombok.Getter;

@Getter
public class CommonException extends RuntimeException {

    private final ErrorCode errorCode;

    public CommonException(ErrorCode errorCode) {
        super(errorCode.message());
        this.errorCode = errorCode;
    }

}
