package com.javaProject.mealBookingProject.dto;

import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class viewBookingDto {
    private Long bookingId;
    private LocalDate bookingDate;
}
