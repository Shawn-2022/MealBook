package com.javaProject.mealBookingProject.service;

import com.javaProject.mealBookingProject.dto.PasswordChangeRequestDto;
import com.javaProject.mealBookingProject.dto.PasswordChangeResponseDto;
import com.javaProject.mealBookingProject.customExceptions.IncorrectOldPasswordException;
import com.javaProject.mealBookingProject.customExceptions.UserNotFoundException;
import com.javaProject.mealBookingProject.entity.NotificationTable;
import com.javaProject.mealBookingProject.entity.PasswordChangeLog;
import com.javaProject.mealBookingProject.entity.UserTable;
import com.javaProject.mealBookingProject.repository.NotificationRepository;
import com.javaProject.mealBookingProject.repository.PasswordChangeLogRepository;
import com.javaProject.mealBookingProject.repository.UserTableRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
@Service
@RequiredArgsConstructor
public class passwordService {

    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final UserTableRepository userTableRepository;
    @Autowired
    private final PasswordChangeLogRepository passwordChangeLogRepository;
    @Autowired
    private final NotificationRepository notificationRepository;
    @Autowired
    private HttpServletRequest request;


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
                .message(user.getEmail()+" Changed Password on" +passwordChangeLog.getChangeDate())
                .build();
        notificationRepository.save(notificationTable);

        return new PasswordChangeResponseDto("Password changed successfully");
    }
}
