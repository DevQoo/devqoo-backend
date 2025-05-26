package com.devqoo.backend.user.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = PasswordPolicyValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordPolicy {

    String message() default "10자이상의 영문 대/소문자, 숫자, 특수문자를 사용해 주세요.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
