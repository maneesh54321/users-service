package org.service.user.service.user;

import lombok.extern.slf4j.Slf4j;
import org.service.user.exception.LoginFailedException;
import org.service.user.vo.LoginForm;
import org.service.user.vo.SignupForm;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String registerUser(SignupForm form) {
        User user = new User(
                form.getEmail(),
                form.getFullName(),
                form.getPhoneNo(),
                form.getDob(),
                generateSecurePassword(form.getPassword())
        );
        User savedUser = userRepository.save(user);
        return savedUser.getEmail();
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


    public User login(LoginForm loginForm) throws LoginFailedException {
        try {
            User user = userRepository.getReferenceById(loginForm.getEmail());
            String secureInputPassword = generateSecurePassword(loginForm.getPassword());
            if(user.getSecurePassword().equals(secureInputPassword)) {
                return user;
            } else {
                throw new LoginFailedException("Invalid credentials!!");
            }
        } catch (EntityNotFoundException e){
            throw new LoginFailedException("Invalid credentials!!");
        }
    }
}
