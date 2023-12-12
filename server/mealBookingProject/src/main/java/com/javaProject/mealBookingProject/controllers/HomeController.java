package com.javaProject.mealBookingProject.controllers;

import com.javaProject.mealBookingProject.auth.AuthenticationService;
import com.javaProject.mealBookingProject.auth.authDto.AuthResponse;
import com.javaProject.mealBookingProject.auth.authDto.PasswordChangeRequestDto;
import com.javaProject.mealBookingProject.auth.authDto.PasswordChangeResponseDto;
import com.javaProject.mealBookingProject.auth.authDto.authRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("mealBooking/auth/")
public class HomeController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> userAuthentication(@RequestBody authRequest request){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/change-password")
    public PasswordChangeResponseDto changePassword(@RequestBody PasswordChangeRequestDto passwordChangeRequest) {
        return authenticationService.changePassword(passwordChangeRequest);
    }

}