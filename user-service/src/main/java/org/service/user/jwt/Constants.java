package org.service.user.jwt;

import com.auth0.jwt.algorithms.Algorithm;

public interface Constants {
    String SECRET = "secret";

    Algorithm SIGNING_ALGORITHM = Algorithm.HMAC256(SECRET);

    String ISSUER = "users-service";
}
