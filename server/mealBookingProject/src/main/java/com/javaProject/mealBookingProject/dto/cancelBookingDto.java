package com.javaProject.mealBookingProject.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class cancelBookingDto {
    private Long UserID;
    private LocalDate cancelDate;
}
