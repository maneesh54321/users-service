package org.service.user.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.function.Predicate;

@Component
public class TokenManager {

    private final ObjectMapper objectMapper;

    private static final TokenHeader JWT_TOKEN_HEADER = new TokenHeader("SHA512", TokenType.JWT);

    private static final String SECRET_SALT = "The secret salt of users-service";

    private static final Predicate<Long> hasExpired =
            expiryTime -> Instant.ofEpochMilli(expiryTime).isAfter(Instant.now());

    @Autowired
    public TokenManager(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String createToken(String audience) {
        TokenPayload tokenPayload = new TokenPayload(
                "users-service generated token",
                "users-service",
                audience,
                Instant.now().plusSeconds(300).toEpochMilli()
        );
        try {
            String encodedPayload = base64Encode(objectMapper.writeValueAsString(tokenPayload));
            String encodedHeader = base64Encode(objectMapper.writeValueAsString(JWT_TOKEN_HEADER));
            String signature = createSignature(encodedHeader, encodedPayload, JWT_TOKEN_HEADER.getAlgorithm());
            return encodedHeader + "." + encodedPayload + "." + signature;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String createSignature(String encodedTokenHeader, String encodedTokenPayload, String tokenHeaderAlgorithm) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(tokenHeaderAlgorithm);
            messageDigest.update(SECRET_SALT.getBytes(StandardCharsets.UTF_8));
            messageDigest.update((encodedTokenHeader + "." + encodedTokenPayload).getBytes(StandardCharsets.UTF_8));
            byte[] hashedSignature = messageDigest.digest();
            return base64Encode(hashedSignature);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private String base64Encode(String input) {
        return base64Encode(input.getBytes(StandardCharsets.UTF_8));
    }

    private String base64Encode(byte[] input) {
        return new String(Base64.getEncoder().encode(input));
    }


    public boolean isTokenValid(String token) {
        if (StringUtils.hasText(token)) {
            String[] tokenParts = token.split("\\.");
            if (tokenParts.length == 3) {
                try {
                    String encodedPayload = tokenParts[1];
                    TokenPayload tokenPayload = objectMapper.readValue(Base64.getDecoder().decode(encodedPayload), TokenPayload.class);
                    String encodedHeader = tokenParts[0];
                    TokenHeader tokenHeader = objectMapper.readValue(
                            Base64.getDecoder().decode(encodedHeader),
                            TokenHeader.class
                    );
                    return isTokenUnhampered(
                            encodedHeader,
                            encodedPayload,
                            tokenHeader.getAlgorithm(),
                            tokenParts[2]) &&
                            hasExpired.test(tokenPayload.getExpiryTime()
                            );
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return false;
    }

    private boolean isTokenUnhampered(String encodedHeader, String encodedPayload, String algorithm, String signature) {
        String calculatedSignature = createSignature(
                encodedHeader,
                encodedPayload,
                algorithm
        );
        return calculatedSignature.equals(signature);
    }
}
