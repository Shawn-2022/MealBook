package com.javaProject.mealBookingProject.auth;

import com.javaProject.mealBookingProject.auth.authDto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("mealBooking/admin/")
@RequiredArgsConstructor
public class AuthenticationController {

    @Autowired
    private final AuthenticationService authenticationService;

    //shyam
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/empRegister")
    public ResponseEntity<AuthResponse> employeeRegister(@RequestBody registerRequest request){
        return ResponseEntity.ok(authenticationService.register(request));
    }

//    @PreAuthorize("hasRole('ADMIN')")
//    @GetMapping("/hello")
//    public ResponseEntity<String> sayHello() {
//        return ResponseEntity.ok("Hello to admin");
//    }

}
