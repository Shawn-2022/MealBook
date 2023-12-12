package com.javaProject.mealBookingProject.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingHelperDto {
    private Long userId;
    private LocalDate bookingDate;}
