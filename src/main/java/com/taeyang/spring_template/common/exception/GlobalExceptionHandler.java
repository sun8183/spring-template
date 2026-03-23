package com.taeyang.spring_template.common.exception;

import com.taeyang.spring_template.common.exception.dto.ErrorResponse;
import com.taeyang.spring_template.common.exception.dto.FieldErrorResponse;
import com.taeyang.spring_template.common.exception.enums.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Valid 실패 시 실행되는 익셉션
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {
        List<FieldErrorResponse> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldErrorResponse::new)
                .toList();
        return createErrorResponse(errors);
    }

    // 2. 비즈니스 에러 정의 메서드
    @ExceptionHandler(CommonException.class)
    public ResponseEntity<ErrorResponse> handleCustom(CommonException e) {
        return createErrorResponse(e.getErrorCode());
    }

    // 3. 위에서 잡지 못한 에러 공통 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return createErrorResponse(ErrorCode.INTERNAL_ERROR);
    }

    private ResponseEntity<ErrorResponse> createErrorResponse(Object details) {
        return ResponseEntity
                .status(ErrorCode.INVALID_INPUT.status())
                .body(ErrorResponse.of(ErrorCode.INVALID_INPUT, details));
    }

    private ResponseEntity<ErrorResponse> createErrorResponse(ErrorCode code) {
        return ResponseEntity
                .status(code.status())
                .body(ErrorResponse.of(code, null));
    }
}