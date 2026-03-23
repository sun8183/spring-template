package com.taeyang.spring_template.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented // 문서에 추가할 것인가 나타내는 어노테이션
@Constraint(validatedBy = {})   // 내부 패턴으로 검증하여 공백
@NotBlank(message = "비밀번호는 필수입니다.")
@Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}\\[\\]:;\"'<>,.?/]).{8,16}$",
        message = "비밀번호는 8~16자이며, 영문/숫자/특수문자를 각각 최소 1개 포함해야 합니다."
)
public @interface ValidPw {
    // 기본 메시지
    String message() default "비밀번호 형식이 올바르지 않습니다.";

    // 검증 그룹 나누기용
    Class<?>[] groups() default {};

    // 검증 결과 추가정보 붙일 때 사용
    Class<? extends Payload>[] payload() default {};
}
