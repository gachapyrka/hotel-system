package com.example.hotel.domain;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN,
    EMPLOYEE,
    USER;

    @Override
    public String getAuthority() {
        return name();
    }
}
