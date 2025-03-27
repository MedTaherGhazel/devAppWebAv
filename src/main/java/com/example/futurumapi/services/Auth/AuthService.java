package com.example.futurumapi.services.Auth;

import com.example.futurumapi.dto.UserRegistrationDto;
import com.example.futurumapi.entities.Role;
import com.example.futurumapi.entities.User;
import com.example.futurumapi.repositories.UserRepository;
import com.example.futurumapi.security.CustomUserDetails;
import com.example.futurumapi.security.jwt.JwtTokenUtil;
import com.example.futurumapi.security.requests.AuthRequest;
import com.example.futurumapi.security.responses.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    public User registerUser(UserRegistrationDto registrationDto) {
        User user = new User();
        user.setFname(registrationDto.getFname());
        user.setLname(registrationDto.getLname());
        user.setEmail(registrationDto.getEmail());
        user.setUsername(registrationDto.getUsername());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setRole(Role.USER);

        return userRepository.save(user);
    }

    public AuthResponse login(AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()
                )
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        return new AuthResponse(
                jwtTokenUtil.generateToken(userDetails),
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getFname(),
                user.getLname(),
                user.getRole().name()
        );
    }

    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }

    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }
}