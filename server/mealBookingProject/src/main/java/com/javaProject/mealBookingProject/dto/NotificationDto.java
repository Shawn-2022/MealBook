package com.javaProject.mealBookingProject.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    private long notificationId;
    private String role;
    private boolean Unread;
    private String message;

}
