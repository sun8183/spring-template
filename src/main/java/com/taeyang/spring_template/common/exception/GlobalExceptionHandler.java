package com.taeyang.spring_template.common.exception;

import com.taeyang.spring_template.common.exception.dto.FieldErrorResponse;
import com.taeyang.spring_template.common.exception.enums.ErrorCode;
import com.taeyang.spring_template.common.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

import static com.taeyang.spring_template.common.exception.enums.ErrorCode.INTERNAL_ERROR;
import static com.taeyang.spring_template.common.exception.enums.ErrorCode.INVALID_INPUT;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * [1] @Valid 유효성 검사 실패 시 발생
     * 클라이언트에게 어떤 필드가 잘못되었는지 상세 목록(data)을 전달합니다.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<List<FieldErrorResponse>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn("handleMethodArgumentNotValidException", e);
        List<FieldErrorResponse> errors = FieldErrorResponse.of(e.getBindingResult());
        return createErrorResponse(INVALID_INPUT, errors);
    }

    /**
     * [2] 비즈니스 로직 예외 (CommonException) 처리
     */
    @ExceptionHandler(CommonException.class)
    public ResponseEntity<ApiResponse<Void>> handleCommonException(CommonException e) {
        log.warn("handleCommonException", e);
        return createErrorResponse(e.getErrorCode(), null);
    }

    /**
     * [3] 그 외 예상치 못한 모든 예외 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("handleException", e);
        return createErrorResponse(INTERNAL_ERROR, null);
    }

    /**
     * 공통 응답 생성을 위한 유틸리티 메서드
     */
    private <T> ResponseEntity<ApiResponse<T>> createErrorResponse(ErrorCode code, T data) {
        return ResponseEntity
                .status(code.getStatus())
                .body(ApiResponse.error(code, data));
    }
}