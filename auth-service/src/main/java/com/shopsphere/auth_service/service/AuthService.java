package com.shopsphere.auth_service.service;

import com.shopsphere.auth_service.dto.RegisterRequest;
import com.shopsphere.auth_service.entity.Role;
import com.shopsphere.auth_service.entity.User;
import com.shopsphere.auth_service.exception.UserAlreadyExistsException;
import com.shopsphere.auth_service.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final AuthRepository authRepository;
    public String register(RegisterRequest registerRequest){
        if(authRepository.existsByEmail(registerRequest.email())){
            throw new UserAlreadyExistsException("User already exists");
        }
        User user=User.builder()
                .email(registerRequest.email())
                .name(registerRequest.name())
                .role(Role.USER)
                .password(passwordEncoder.encode(registerRequest.password()))
                .build();
        authRepository.save(user);
        return "Registered successfully";
    }
}
