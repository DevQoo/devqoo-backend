package com.devqoo.backend.user.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, PasswordConfirmable> {

    private String message;
    private String fieldName;

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {

        message = constraintAnnotation.message();
        fieldName = constraintAnnotation.fieldName();
    }

    @Override
    public boolean isValid(PasswordConfirmable passwordConfirmable, ConstraintValidatorContext context) {

        String password = passwordConfirmable.password();
        String passwordConfirm = passwordConfirmable.passwordConfirm();

        if (password.equals(passwordConfirm)) {
            return true;
        }

        addViolation(context);
        return false;
    }

    private void addViolation(ConstraintValidatorContext context) {

        // Object Error 메시지 막기
        context.disableDefaultConstraintViolation();

        // Custom 메시지 설정
        context
            .buildConstraintViolationWithTemplate(message)
            .addPropertyNode(fieldName)
            .addConstraintViolation();
    }
}
