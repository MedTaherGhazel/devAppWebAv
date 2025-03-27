package com.example.futurumapi.security.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private Long id;
    private String email;
    private String username;
    private String fname;
    private String lname;
    private String role;

}