package com.shopsphere.auth_service.service;

import com.shopsphere.auth_service.entity.User;
import com.shopsphere.auth_service.repository.AuthRepository;
import com.shopsphere.auth_service.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found with email: " + email
                        ));

        return new CustomUserDetails(user);
    }
}