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
                .user_Name(request.getUser_name())
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


    public PasswordChangeResponseDto changePassword(PasswordChangeRequestDto passwordChangeRequestDto) {
        Long userId = (Long) request.getAttribute("userId");
        UserTable user = userTableRepository.findById(userId)
                .orElseThrow(() ->  new UserNotFoundException("User not found with email: " + userId));
        String oldPassword = passwordChangeRequestDto.getOldPassword();
        String newPassword = passwordChangeRequestDto.getNewPassword();

        String storedPassword = user.getPassword();
        // Verify the old password
        if (oldPassword == null || !passwordEncoder.matches(oldPassword, storedPassword)) {
            throw new IncorrectOldPasswordException("Incorrect old password");
        }

        String encodedOldPassword = passwordEncoder.encode(oldPassword);
        String encodedNewPassword = passwordEncoder.encode(newPassword);

        // Update the password
        user.setPassword(encodedNewPassword);
        userTableRepository.save(user);

        PasswordChangeLog passwordChangeLog = PasswordChangeLog.builder()
                .oldPassword(encodedOldPassword)
                .newPassword(encodedNewPassword)
                .changeDate(new Timestamp(System.currentTimeMillis()))
                .userEmail(user)
                .build();
        passwordChangeLogRepository.save(passwordChangeLog);

        NotificationTable notificationTable= NotificationTable.builder()
                .role(user.getRole().name())
                .userID(user)
                .unread(true)
                .message(user.getUser_Name()+" Changed Password on" +passwordChangeLog.getChangeDate())
                .build();
        notificationRepository.save(notificationTable);

        return new PasswordChangeResponseDto("Password changed successfully");
    }
}
