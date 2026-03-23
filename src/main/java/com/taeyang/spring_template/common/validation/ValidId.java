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
@NotBlank(message = "아이디는 필수입니다.")
@Pattern(
        regexp = "^[a-zA-Z0-9]{8,16}$",
        message = "아이디는 8~16자 영문, 숫자만 가능합니다."
)
public @interface ValidId {
    String message() default "아이디 형식이 올바르지 않습니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
