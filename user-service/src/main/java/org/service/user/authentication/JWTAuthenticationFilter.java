package org.service.user.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.service.user.jwt.TokenManager;
import org.service.user.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JWTAuthenticationFilter extends GenericFilterBean {

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        if(((HttpServletRequest) request).getRequestURI().matches(".*(swagger|login|signup|\\/v2\\/api-docs)+.*")) {
            chain.doFilter(request, response);
        } else {
            String jwtToken = httpServletRequest.getCookies()[0].getValue();
            log.info("Token in the request is: {}", jwtToken);
            if(tokenManager.isTokenValid(jwtToken)){
                chain.doFilter(request, response);
            } else {
                Response authResponse = new Response("Unauthenticated user", HttpStatus.UNAUTHORIZED);
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                httpServletResponse.setStatus(401);
                response.getOutputStream().write(objectMapper.writeValueAsBytes(authResponse));
            }
        }
    }
}
