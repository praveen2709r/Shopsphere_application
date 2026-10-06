package com.shopsphere.auth_service.config;

import com.shopsphere.auth_service.entity.Role;
import com.shopsphere.auth_service.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){
        httpSecurity
                .csrf(csrf->csrf.disable())
                .authorizeHttpRequests(
                        auth->auth.requestMatchers(
                                "/api/v1/auth/register","/api/v1/auth/login"
                        ).permitAll()
                                .requestMatchers(
                                        "/api/v1/auth/admin"
                                ).hasRole(Role.ADMIN.name())
                                .requestMatchers(
                                        "/api/v1/auth/me"
                                ).hasAnyRole(Role.ADMIN.name(),Role.USER.name())
                                .anyRequest().authenticated()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
                .sessionManagement(
                        session->session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        return httpSecurity.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration){
        return authenticationConfiguration.getAuthenticationManager();
    }
}
