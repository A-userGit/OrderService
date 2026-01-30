package com.innowise.orderservice.util;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;

public class SecurityUtil {
    public static String getAuthenticatedUserEmail() {
        String email;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                email = ((UserDetails) principal).getUsername();
            } else {
                if (principal instanceof Jwt) {
                    email = authentication.getName();
                }else {
                    throw new AuthenticationServiceException("Invalid auth token format.");
                }
            }
        } else {
            throw  new AuthenticationServiceException("Not authenticated.");
        }
        return email;
    }
}
