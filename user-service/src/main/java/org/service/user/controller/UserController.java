package org.service.user.controller;

import org.service.user.exception.ValidationException;
import org.service.user.service.UserService;
import org.service.user.vo.Response;
import org.service.user.vo.SignupForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

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
}
