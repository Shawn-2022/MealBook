package com.javaProject.mealBookingProject.auth;

import com.javaProject.mealBookingProject.auth.authDto.*;
import com.javaProject.mealBookingProject.config.JwtService;
import com.javaProject.mealBookingProject.customExceptions.*;
import com.javaProject.mealBookingProject.entity.NotificationTable;
import com.javaProject.mealBookingProject.entity.PasswordChangeLog;
import com.javaProject.mealBookingProject.entity.UserTable;
import com.javaProject.mealBookingProject.repository.NotificationRepository;
import com.javaProject.mealBookingProject.repository.PasswordChangeLogRepository;
import com.javaProject.mealBookingProject.repository.UserTableRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserTableRepository userTableRepository;
    private final PasswordChangeLogRepository passwordChangeLogRepository;
    private final NotificationRepository notificationRepository;
    private final UserTable userTable;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    private HttpServletRequest request;



    public AuthResponse register(registerRequest request) {
        if (userTableRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateEmailException("Email already Exists");
        }

        var user = UserTable.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .role(UserTable.UserRole.ROLE_EMPLOYEE)
                .build();

        var savedUser = userTableRepository.save(user);

        String jwtToken;
        try {
            jwtToken = jwtService.generateToken(savedUser);
        } catch (Exception e) {
            throw new TokenGenerationException("Error generating JWT token.", e);
        }
        return AuthResponse.builder()
                .token(jwtToken).build();
    }
    // add exceptions

    public AuthResponse authenticate(authRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException("Invalid email or password.");
        }

        var savedUser = userTableRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + request.getEmail()));

        // last login
//        var lastLogin =UserTable.builder()
//                .lastLogin(new Timestamp(System.currentTimeMillis()))
//                .build();
//        userTableRepository.save(lastLogin);
//        savedUser.setLastLogin(new Timestamp(System.currentTimeMillis()));
        String jwtToken;
        try {
            jwtToken = jwtService.generateToken(savedUser);
        } catch (Exception e) {
            throw new TokenGenerationException("Error generating JWT token.", e);
        }
        return AuthResponse.builder()
                .token(jwtToken).build();
    }

}
