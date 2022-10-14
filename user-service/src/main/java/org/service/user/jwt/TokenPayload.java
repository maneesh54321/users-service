package org.service.user.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenPayload {
    private String subject;
    private String issuer;
    private String audience;
    private long expiryTime;
}
