package com.javaProject.mealBookingProject.auth;

import com.javaProject.mealBookingProject.auth.AuthenticationService;
import com.javaProject.mealBookingProject.dto.AuthResponse;
import com.javaProject.mealBookingProject.dto.authRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//shyam
@RestController
@RequestMapping("mealBooking")
public class loginController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> userAuthentication(@RequestBody authRequest request){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }


}