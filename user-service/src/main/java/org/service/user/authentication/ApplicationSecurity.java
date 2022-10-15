package org.service.user.authentication;


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
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class ApplicationSecurity {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors().and()
                .csrf().disable()
                .addFilterBefore(new JWTAuthenticationSetupFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers(HttpMethod.POST, "/user/login", "/user/signup").permitAll()
                .antMatchers("/api/**").permitAll()
                .antMatchers("/swagger-ui/**").permitAll()
                .anyRequest().authenticated().and().authenticationProvider(new JWTAuthenticationProvider())
                .exceptionHandling().authenticationEntryPoint(new JWTAuthenticationEntryPoint());
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        return new UrlBasedCorsConfigurationSource();
    }
}
