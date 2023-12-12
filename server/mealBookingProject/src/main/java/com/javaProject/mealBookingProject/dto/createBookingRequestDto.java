package com.javaProject.mealBookingProject.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class createBookingRequestDto {
    private Long UserID;
    private String startDate;
    private String endDate;

}
