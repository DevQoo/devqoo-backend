package com.devqoo.backend.user.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordPolicyValidator implements ConstraintValidator<PasswordPolicy, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {

        if (password.length() < 10) {
            return false;
        }

        boolean upper = password.matches(".*[A-Z].*");
        boolean lower = password.matches(".*[a-z].*");
        boolean digit = password.matches(".*\\d.*");
        boolean special = password.matches(".*[!@#$%^&*()\\-_=+\\[{\\]}|;:'\",<.>/?`~\\\\].*");

        int count = 0;

        if (upper || lower) {
            count++;
        }
        if (digit) {
            count++;
        }
        if (special) {
            count++;
        }

        return count >= 2;
    }
}
