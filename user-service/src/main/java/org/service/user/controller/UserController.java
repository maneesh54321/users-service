package org.service.user.controller;

import org.service.user.exception.LoginFailedException;
import org.service.user.jwt.TokenManager;
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

@RestController
@CrossOrigin
public class UserController {

    private final UserService userService;

    private final TokenManager tokenManager;

    private final Validator validator;

    @Autowired
    public UserController(UserService userService, TokenManager tokenManager, Validator validator) {
        this.userService = userService;
        this.tokenManager = tokenManager;
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
                response.addData("userId", tokenManager.createToken(user.getEmail()));
            } else {
                response = new Response(validationResult.getErrorMessage(), HttpStatus.BAD_REQUEST);
            }
        } catch (LoginFailedException e) {
            response = new Response(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return response;
    }
}
