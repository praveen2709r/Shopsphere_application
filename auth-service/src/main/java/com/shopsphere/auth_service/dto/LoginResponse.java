package com.shopsphere.auth_service.dto;

public record LoginResponse(
        String accessToken,
        String tokenType
) {
}