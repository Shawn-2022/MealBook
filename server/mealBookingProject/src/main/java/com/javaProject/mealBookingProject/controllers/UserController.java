package com.javaProject.mealBookingProject.controllers;

import com.javaProject.mealBookingProject.customExceptions.NotificationNotFoundException;
import com.javaProject.mealBookingProject.customExceptions.UserNotFoundException;
import com.javaProject.mealBookingProject.dto.NotificationDto;
import com.javaProject.mealBookingProject.dto.UserDto;
import com.javaProject.mealBookingProject.entity.NotificationTable;
import com.javaProject.mealBookingProject.entity.UserTable;
import com.javaProject.mealBookingProject.repository.NotificationRepository;
import com.javaProject.mealBookingProject.repository.UserTableRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("mealBooking/home")
public class UserController {

    private final UserTableRepository userTableRepository;
    private final NotificationRepository notificationRepository;

    @Autowired
    private HttpServletRequest request;

    // get user by id
    @GetMapping("/getUser")
    public ResponseEntity<UserDto> getUserById() {
        Long userId = (Long) request.getAttribute("userId");

        UserTable user = userTableRepository.findById(userId).orElseThrow(() ->  new UserNotFoundException("User not found with email: " + userId));

        var userInfo = UserDto.builder()
                .User_ID(user.getUserID())
                .user_email(user.getEmail())
                .User_name(user.getUser_Name())
                .role(user.getRole().name())
                .build();
        if (userInfo != null) {
            return ResponseEntity.ok(userInfo);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/notification")
    public ResponseEntity<List<NotificationDto>> getUnreadNotificationsByUserId() {
        Long userId = (Long) request.getAttribute("userId");
        UserTable user = userTableRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        List<NotificationTable> unreadNotifications = notificationRepository.getByUserIDAndUnread(user, true);

        if (unreadNotifications != null && !unreadNotifications.isEmpty()) {
            List<NotificationDto> notificationDtos = unreadNotifications.stream()
                    .map(notification -> new NotificationDto(
                            notification.getRole(),
                            notification.isUnread(),
                            notification.getMessage()))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(notificationDtos);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @PutMapping("/notification/{id}")
    public ResponseEntity<String> updateNotificationStatus(@PathVariable Long id) {
        // Get the notification by ID from the repository
        NotificationTable notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException("Notification not found with ID: " + id));

        // Update the notification status to unread=false
        notification.setUnread(false);
        notificationRepository.save(notification);

        return ResponseEntity.ok("Notification status updated successfully");
    }

}