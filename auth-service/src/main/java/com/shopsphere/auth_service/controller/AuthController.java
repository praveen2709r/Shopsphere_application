package com.shopsphere.auth_service.controller;

import com.shopsphere.auth_service.dto.LoginRequest;
import com.shopsphere.auth_service.dto.LoginResponse;
import com.shopsphere.auth_service.dto.RegisterRequest;
import com.shopsphere.auth_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody @Valid RegisterRequest registerRequest
    ){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.register(registerRequest));
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.status(HttpStatus.OK).body(authService.login(loginRequest));
    }
    @GetMapping("/me")
    public String getHome(){
        return "Home page";
    }
    @GetMapping("/admin")
    public String getAdminHome(){
        return "Admin Home page";
    }
}
