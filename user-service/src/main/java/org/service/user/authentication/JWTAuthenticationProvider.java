package org.service.user.authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.service.user.jwt.Constants;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.Collections;

@Slf4j
public class JWTAuthenticationProvider implements AuthenticationProvider {
    private final JWTVerifier jwtVerifier;

    public JWTAuthenticationProvider() {
        this.jwtVerifier = JWT.require(Constants.SIGNING_ALGORITHM)
                .withIssuer(Constants.ISSUER)
                .build();
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!authentication.isAuthenticated()) {
            try {
                String jwtToken = (String) authentication.getCredentials();
                DecodedJWT decodedJWT = jwtVerifier.verify(jwtToken);
                JWTAuthentication authenticationToken = new JWTAuthentication(null, decodedJWT);
                authenticationToken.setAuthenticated(true);
                return authenticationToken;
            } catch (JWTVerificationException exception) {
                throw new BadCredentialsException("Unauthenticated user!!");
            }
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
