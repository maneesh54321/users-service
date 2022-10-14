package org.service.user.validation;

import org.service.user.vo.LoginForm;
import org.service.user.vo.SignupForm;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class Validator {

    public ValidationResult validateSignupForm(SignupForm signupForm) {
        ValidationResult validationResult = new ValidationResult();
        if (!StringUtils.hasText(signupForm.getFullName()) && signupForm.getFullName().length() <= 2) {
            validationResult.setValid(false);
            validationResult.addError("Name must contain at least two characters!!");
        }
        if (!StringUtils.hasText(signupForm.getPhoneNo()) && !signupForm.getPhoneNo().matches("[0-9+-]{8,}")) {
            validationResult.setValid(false);
            validationResult.addError("Phone number must contain at least 8 characters, valid characters are: [0-9 + or -] !!");
        }
        if (!StringUtils.hasText(signupForm.getEmail()) && !signupForm.getEmail().matches(".*@.*\\..*")) {
            validationResult.setValid(false);
            validationResult.addError("Email must be in the format abc@xyz.com");
        }
        if (!StringUtils.hasText(signupForm.getPassword()) && signupForm.getPassword().length() < 8) {
            validationResult.setValid(false);
            validationResult.addError("Password must contain at least 8 characters");
        }
        return validationResult;
    }

    public ValidationResult validateLoginForm(LoginForm loginForm) {
        return new ValidationResult();
    }
}
