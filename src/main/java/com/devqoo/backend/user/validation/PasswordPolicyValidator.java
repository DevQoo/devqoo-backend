package com.devqoo.backend.user.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PasswordPolicyValidator implements ConstraintValidator<PasswordPolicy, String> {

    private static final Pattern ALPHABET = Pattern.compile(".*[a-zA-Z].*");
    private static final Pattern DIGIT = Pattern.compile(".*\\d.*");
    private static final Pattern SPECIAL =
        Pattern.compile(".*[!@#$%^&*()\\-_=+\\[{\\]}|;:'\",<.>/?`~\\\\].*");


    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {

        if (password.length() < 10) {
            return false;
        }

        int count = 0;
        if (ALPHABET.matcher(password).matches()) {
            count++;
        }
        if (DIGIT.matcher(password).matches()) {
            count++;
        }
        if (SPECIAL.matcher(password).matches()) {
            count++;
        }

        return count >= 2;
    }
}
