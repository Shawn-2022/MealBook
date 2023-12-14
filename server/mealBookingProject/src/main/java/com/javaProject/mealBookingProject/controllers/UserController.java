package com.javaProject.mealBookingProject.controllers;

import com.javaProject.mealBookingProject.auth.AuthenticationService;
import com.javaProject.mealBookingProject.auth.authDto.PasswordChangeRequestDto;
import com.javaProject.mealBookingProject.auth.authDto.PasswordChangeResponseDto;
import com.javaProject.mealBookingProject.customExceptions.NotificationNotFoundException;
import com.javaProject.mealBookingProject.customExceptions.UserNotFoundException;
import com.javaProject.mealBookingProject.dto.NotificationDto;
import com.javaProject.mealBookingProject.dto.UserDto;
import com.javaProject.mealBookingProject.entity.NotificationTable;
import com.javaProject.mealBookingProject.entity.UserTable;
import com.javaProject.mealBookingProject.repository.NotificationRepository;
import com.javaProject.mealBookingProject.repository.UserTableRepository;
import com.javaProject.mealBookingProject.service.homeService;
import com.javaProject.mealBookingProject.service.passwordService;
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

    @Autowired
    private final homeService homeservice;
    private final passwordService changePassword;

    private AuthenticationService authenticationService;

    @Autowired
    private HttpServletRequest request;

    //shyam
    // get user by id
    @GetMapping("/getUser")
    public ResponseEntity<UserDto> getUserById() {
        Long userId = (Long) request.getAttribute("userId");
        UserDto userInfo = homeservice.getUserById(userId);
        if (userInfo != null) {
            return ResponseEntity.ok(userInfo);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    //Jayati
    @PostMapping("/change-password")
    public PasswordChangeResponseDto changePassword(@RequestBody PasswordChangeRequestDto passwordChangeRequest) {
        return changePassword.changePassword(passwordChangeRequest);
    }




}