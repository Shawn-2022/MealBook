package com.javaProject.mealBookingProject.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private long User_ID;
    private String firstName;
    private String lastName;
    private String user_email;
    private String role;
}
