package com.javaProject.mealBookingProject.service;


import com.javaProject.mealBookingProject.customExceptions.NotificationNotFoundException;
import com.javaProject.mealBookingProject.customExceptions.UserNotFoundException;
import com.javaProject.mealBookingProject.dto.NotificationDto;
import com.javaProject.mealBookingProject.entity.NotificationTable;
import com.javaProject.mealBookingProject.entity.UserTable;
import com.javaProject.mealBookingProject.repository.NotificationRepository;
import com.javaProject.mealBookingProject.repository.UserTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class notificationService {

    private final NotificationRepository notificationRepository;
    private final UserTableRepository userTableRepository;

    public List<NotificationDto> getUnreadNotificationsByUserId(Long userId) {
        UserTable user = userTableRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        List<NotificationTable> unreadNotifications = notificationRepository.getByUserIDAndUnread(user, true);

        if (unreadNotifications != null && !unreadNotifications.isEmpty()) {
            return unreadNotifications.stream()
                    .map(notification -> new NotificationDto(
                            notification.getNotificationId(),
                            notification.getRole(),
                            notification.isUnread(),
                            notification.getMessage()))
                    .collect(Collectors.toList());
        } else {
            throw new NotificationNotFoundException("No unread notifications found for user ID: " + userId);
        }
    }

    public void updateNotificationStatus(Long id) {
        NotificationTable notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException("Notification not found with ID: " + id));

        notification.setUnread(false);
        notificationRepository.save(notification);
    }

}
