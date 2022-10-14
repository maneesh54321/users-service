package org.service.user.controller;

import org.service.user.exception.LoginFailedException;
import org.service.user.exception.ValidationException;
import org.service.user.jwt.TokenManager;
import org.service.user.service.User;
import org.service.user.service.UserService;
import org.service.user.vo.LoginForm;
import org.service.user.vo.Response;
import org.service.user.vo.SignupForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenManager tokenManager;

    @PostMapping("/user/signup")
    public Response signupUser(@RequestBody SignupForm signupForm) {
        Response response;
        try {
            String userId = userService.registerUser(signupForm);
            response = new Response("User signed up successfully!!", HttpStatus.OK);
            response.addData("userId", userId);
        } catch (ValidationException e) {
            response = new Response(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @PostMapping("/user/login")
    public Response loginUser(@RequestBody LoginForm loginForm) {
        Response response;
        try {
            Optional<User> maybeUser = userService.login(loginForm);
            if(maybeUser.isPresent()) {
                response = new Response("user logged in successfully!!", HttpStatus.OK);
                response.addData("userId", tokenManager.createToken(maybeUser.get()));
            } else {
                response = new Response("Failed to login!!", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (LoginFailedException e) {
            response = new Response(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return response;
    }
}
