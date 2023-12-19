package com.javaProject.mealBookingProject.controllers;


import com.javaProject.mealBookingProject.customExceptions.NotificationNotFoundException;
import com.javaProject.mealBookingProject.customExceptions.UserNotFoundException;
import com.javaProject.mealBookingProject.dto.NotificationDto;
import com.javaProject.mealBookingProject.entity.NotificationTable;
import com.javaProject.mealBookingProject.entity.UserTable;
import com.javaProject.mealBookingProject.repository.NotificationRepository;
import com.javaProject.mealBookingProject.repository.UserTableRepository;
import com.javaProject.mealBookingProject.service.notificationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

//shyam
@RequiredArgsConstructor
@RestController
@RequestMapping("mealBooking/home")
public class notificationController {

    @Autowired
    private final notificationService notification;

    @Autowired
    private HttpServletRequest request;

    @GetMapping("/notification")
    public ResponseEntity<List<NotificationDto>> getUnreadNotificationsByUserId() {
        Long userId = (Long) request.getAttribute("userId");
        List<NotificationDto> notificationDtos = notification.getUnreadNotificationsByUserId(userId);
        return ResponseEntity.ok(notificationDtos);
    }

    @PutMapping("/notification/{id}")
    public ResponseEntity<String> updateNotificationStatus(@PathVariable Long id) {
        notification.updateNotificationStatus(id);
        return ResponseEntity.ok("Notification status updated successfully");
    }

}
