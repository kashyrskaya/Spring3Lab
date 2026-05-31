package lt.esdc.service;

import lt.esdc.dto.auth.AuthResponse;
import lt.esdc.dto.auth.LoginRequest;
import lt.esdc.dto.auth.RefreshTokenRequest;
import lt.esdc.dto.auth.RegisterRequest;
import lt.esdc.model.Role;
import lt.esdc.model.User;
import lt.esdc.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserService userService, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest request) {
        Role role = request.role() != null ? request.role() : Role.USER;
        
        User user = new User(request.username(), passwordEncoder.encode(request.password()), role);
        userService.saveUser(user);
        
        return new AuthResponse(jwtService.generateToken(user), jwtService.generateRefreshToken(user));
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        UserDetails user = userService.loadUserByUsername(request.username());
        return new AuthResponse(jwtService.generateToken(user), jwtService.generateRefreshToken(user));
    }

    public AuthResponse refresh(RefreshTokenRequest request) {
        String username = jwtService.extractUsername(request.refreshToken());
        UserDetails user = userService.loadUserByUsername(username);
        
        if (jwtService.isTokenValid(request.refreshToken(), user)) {
            return new AuthResponse(jwtService.generateToken(user), request.refreshToken());
        }
        throw new RuntimeException("Invalid refresh token!");
    }
}