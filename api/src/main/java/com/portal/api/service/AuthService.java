package com.portal.api.service;

import com.portal.api.auth.UserAuth;
import com.portal.api.dto.LoginRequest;
import com.portal.api.dto.RegisterRequest;
import com.portal.api.dto.TokenResponse;
import com.portal.api.dto.UserResponse;
import com.portal.api.entity.User;
import com.portal.api.exception.UserAlreadyExistsException;
import com.portal.api.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager,
                       TokenService tokenService,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public TokenResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(request.username(), request.password()));
        return tokenService.generateToken((UserAuth) authentication.getPrincipal());
    }

    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new UserAlreadyExistsException("Nome de usuário já cadastrado");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException("E-mail já cadastrado");
        }

        User user = new User(request.username(), request.email(), passwordEncoder.encode(request.password()));
        return UserResponse.from(userRepository.save(user));
    }
}
