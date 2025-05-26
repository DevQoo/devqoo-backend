package com.devqoo.backend.user.validation;

import com.devqoo.backend.user.dto.form.SignUpForm;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, SignUpForm> {

    private String message;
    private String fieldName;

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {

        message = constraintAnnotation.message();
        fieldName = constraintAnnotation.fieldName();
    }

    @Override
    public boolean isValid(SignUpForm signUpForm, ConstraintValidatorContext context) {

        String password = signUpForm.password();
        String passwordConfirm = signUpForm.passwordConfirm();

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
