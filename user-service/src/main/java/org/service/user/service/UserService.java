package org.service.user.service;

import lombok.extern.slf4j.Slf4j;
import org.service.user.exception.LoginFailedException;
import org.service.user.exception.ValidationException;
import org.service.user.repository.UserRepository;
import org.service.user.vo.LoginForm;
import org.service.user.vo.SignupForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public String registerUser(SignupForm form) throws ValidationException {
        ValidationResult validationResult = validateSignupForm(form);
        if (validationResult.isValid()) {
            User user = new User(
                    form.getEmail(),
                    form.getFullName(),
                    form.getPhoneNo(),
                    form.getDob(),
                    generateSecurePassword(form.getPassword())
            );
            User savedUser = userRepository.save(user);
            return savedUser.getEmail();
        } else {
            throw new ValidationException(validationResult.getErrorMessage());
        }
    }

    private String generateSecurePassword(String plainTextPassword) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] passwordHash = messageDigest.digest(plainTextPassword.getBytes(StandardCharsets.UTF_8));
            return new String(Base64.getEncoder().encode(passwordHash));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private ValidationResult validateSignupForm(SignupForm signupForm) {
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


    public Optional<User> login(LoginForm loginForm) throws LoginFailedException {
        ValidationResult validationResult = validateLoginForm(loginForm);
        if (validationResult.isValid()) {
            try {
                User user = userRepository.getReferenceById(loginForm.getEmail());
                String secureInputPassword = generateSecurePassword(loginForm.getPassword());
                if(user.getSecurePassword().equals(secureInputPassword)) {
                    return Optional.of(user);
                }
            } catch (EntityNotFoundException e){
                throw new LoginFailedException("Invalid credentials!!");
            }
        }
        throw new LoginFailedException("Invalid credentials!!");
    }

    private ValidationResult validateLoginForm(LoginForm loginForm) {
        return new ValidationResult();
    }
}
