package org.service.user.controller;

import com.auth0.jwt.JWT;
import org.service.user.exception.LoginFailedException;
import org.service.user.jwt.Constants;
import org.service.user.service.user.User;
import org.service.user.service.user.UserService;
import org.service.user.validation.ValidationResult;
import org.service.user.validation.Validator;
import org.service.user.vo.LoginForm;
import org.service.user.vo.Response;
import org.service.user.vo.SignupForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
public class UserController {

    private final UserService userService;

    private final Validator validator;

    @Autowired
    public UserController(UserService userService, Validator validator) {
        this.userService = userService;
        this.validator = validator;
    }

    @PostMapping("/user/signup")
    public Response signupUser(@RequestBody SignupForm signupForm) {
        Response response;
        ValidationResult validationResult = validator.validateSignupForm(signupForm);
        if(validationResult.isValid()){
            String userId = userService.registerUser(signupForm);
            response = new Response("User signed up successfully!!", HttpStatus.OK);
            response.addData("userId", userId);
        } else {
            response = new Response(validationResult.getErrorMessage(), HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @PostMapping("/user/login")
    public Response loginUser(@RequestBody LoginForm loginForm) {
        Response response;
        try {
            ValidationResult validationResult = validator.validateLoginForm(loginForm);
            if(validationResult.isValid()) {
                User user = userService.login(loginForm);
                response = new Response("user logged in successfully!!", HttpStatus.OK);
                response.addData(
                        "token",
                        JWT.create().withIssuer(Constants.ISSUER).withExpiresAt(Instant.now().plusSeconds(1800))
                        .withSubject(user.getEmail()).sign(Constants.SIGNING_ALGORITHM)
                );
            } else {
                response = new Response(validationResult.getErrorMessage(), HttpStatus.BAD_REQUEST);
            }
        } catch (LoginFailedException e) {
            response = new Response(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return response;
    }
}
