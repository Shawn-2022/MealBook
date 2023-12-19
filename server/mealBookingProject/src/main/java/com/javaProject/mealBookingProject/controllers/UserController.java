package com.javaProject.mealBookingProject.controllers;

import com.javaProject.mealBookingProject.auth.AuthenticationService;
import com.javaProject.mealBookingProject.dto.PasswordChangeRequestDto;
import com.javaProject.mealBookingProject.dto.PasswordChangeResponseDto;
import com.javaProject.mealBookingProject.dto.UserDto;
import com.javaProject.mealBookingProject.service.homeService;
import com.javaProject.mealBookingProject.service.passwordService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("mealBooking/home")
public class UserController {

    @Autowired
    private final homeService homeservice;
    @Autowired
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