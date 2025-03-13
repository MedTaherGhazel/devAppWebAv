package com.example.futurumapi.security;

import com.example.futurumapi.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
        System.out.println("User role: " + user.getRole()); // Debugging
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        System.out.println("User role in getAuthorities: " + user.getRole()); // Debugging
        if (user.getRole() == null) {
            return Collections.emptyList(); // Handle null roles gracefully
        }
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // Use email as the username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Account never expires
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Account is never locked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Credentials never expire
    }

    @Override
    public boolean isEnabled() {
        return true; // Account is always enabled
    }

    // Optional: Add a method to get the actual User entity
    public User getUser() {
        return user;
    }
}