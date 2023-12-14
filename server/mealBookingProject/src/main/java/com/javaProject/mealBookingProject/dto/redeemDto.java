package com.javaProject.mealBookingProject.dto;

import com.javaProject.mealBookingProject.entity.UserTable;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class redeemDto {
    private Long bookingId;
    private LocalDate bookingDate;
    private Long userID;

    private String firstName;
    private String lastName;
    private String Coupon;
}
