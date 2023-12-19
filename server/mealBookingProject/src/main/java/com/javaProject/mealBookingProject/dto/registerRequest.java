package com.javaProject.mealBookingProject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class registerRequest {

    private String email;
    private String password;
    private String firstname;
    private String lastname;
}
