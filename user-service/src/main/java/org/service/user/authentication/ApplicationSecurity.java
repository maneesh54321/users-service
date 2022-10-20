package org.service.user.authentication;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@Slf4j
public class ApplicationSecurity {

    private static final String[] SWAGGER_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/v2/api-docs",
            "/webjars/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors().and()
                .csrf().disable()
                .addFilterBefore(new JWTAuthenticationSetupFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers(SWAGGER_WHITELIST).permitAll()
                .antMatchers(HttpMethod.POST, "/user/login", "/user/signup").permitAll()
                .anyRequest().authenticated().and().authenticationProvider(new JWTAuthenticationProvider())
                .exceptionHandling().authenticationEntryPoint(new JWTAuthenticationEntryPoint());
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsFilter corsFilter(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setMaxAge(1800l);
        corsConfiguration.setAllowedOrigins(Collections.singletonList("*"));
        corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
        corsConfiguration.setExposedHeaders(Collections.singletonList("*"));
        corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }

}
