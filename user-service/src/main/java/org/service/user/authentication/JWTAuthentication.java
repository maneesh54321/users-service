package org.service.user.authentication;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JWTAuthentication extends AbstractAuthenticationToken {

    private final DecodedJWT decodedJWT;

    public JWTAuthentication(Collection<? extends GrantedAuthority> authorities, DecodedJWT decodedJWT) {
        super(authorities);
        this.decodedJWT = decodedJWT;
    }

    @Override
    public Object getCredentials() {
        return decodedJWT.getToken();
    }

    @Override
    public Object getPrincipal() {
        return decodedJWT.getSubject();
    }
}
