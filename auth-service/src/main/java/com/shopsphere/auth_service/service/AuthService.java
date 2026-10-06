package com.shopsphere.auth_service.service;

import com.shopsphere.auth_service.dto.LoginRequest;
import com.shopsphere.auth_service.dto.LoginResponse;
import com.shopsphere.auth_service.dto.RegisterRequest;
import com.shopsphere.auth_service.entity.Role;
import com.shopsphere.auth_service.entity.User;
import com.shopsphere.auth_service.exception.UserAlreadyExistsException;
import com.shopsphere.auth_service.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final AuthRepository authRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
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
    public LoginResponse login(LoginRequest loginRequest){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.password()
                )
        );
        User user=authRepository.findByEmail(loginRequest.email()).orElseThrow(
                ()->new UsernameNotFoundException("User not found")
        );
        String token=jwtService.generateToken(user.getEmail(),user.getRole());
        return new LoginResponse(token,"Bearer");
    }
}
