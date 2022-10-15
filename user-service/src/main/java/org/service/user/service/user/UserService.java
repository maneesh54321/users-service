package org.service.user.service.user;

import lombok.extern.slf4j.Slf4j;
import org.service.user.exception.LoginFailedException;
import org.service.user.vo.LoginForm;
import org.service.user.vo.SignupForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String registerUser(SignupForm form) {
        User user = new User(
                form.getEmail(),
                form.getFullName(),
                form.getPhoneNo(),
                form.getDob(),
                passwordEncoder.encode(form.getPassword())
        );
        User savedUser = userRepository.save(user);
        return savedUser.getEmail();
    }


    public User login(LoginForm loginForm) throws LoginFailedException {
        try {
            User user = userRepository.getReferenceById(loginForm.getEmail());
            String secureInputPassword = passwordEncoder.encode(loginForm.getPassword());
            if(passwordEncoder.matches(loginForm.getPassword(),secureInputPassword)) {
                return user;
            } else {
                throw new LoginFailedException("Invalid credentials!!");
            }
        } catch (EntityNotFoundException e){
            throw new LoginFailedException("Invalid credentials!!");
        }
    }
}
