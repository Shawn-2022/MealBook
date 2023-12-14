package com.javaProject.mealBookingProject.controllers;

import com.javaProject.mealBookingProject.auth.AuthenticationService;
import com.javaProject.mealBookingProject.auth.authDto.AuthResponse;
import com.javaProject.mealBookingProject.auth.authDto.PasswordChangeRequestDto;
import com.javaProject.mealBookingProject.auth.authDto.PasswordChangeResponseDto;
import com.javaProject.mealBookingProject.auth.authDto.authRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//shyam
@RestController
@RequestMapping("mealBooking")
public class HomeController {
    //todo mapping change in postman
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> userAuthentication(@RequestBody authRequest request){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }


}