package com.javaProject.mealBookingProject.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    private String role;
    private boolean Unread;
    private String message;

}
