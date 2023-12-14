package com.javaProject.mealBookingProject.service;

import com.javaProject.mealBookingProject.customExceptions.UserNotFoundException;
import com.javaProject.mealBookingProject.dto.UserDto;
import com.javaProject.mealBookingProject.entity.MealBookingLog;
import com.javaProject.mealBookingProject.entity.MealBookingTable;
import com.javaProject.mealBookingProject.entity.NotificationTable;
import com.javaProject.mealBookingProject.entity.UserTable;
import com.javaProject.mealBookingProject.repository.MealBookingLogRepository;
import com.javaProject.mealBookingProject.repository.MealBookingTableRepository;
import com.javaProject.mealBookingProject.repository.NotificationRepository;
import com.javaProject.mealBookingProject.repository.UserTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class homeService {

    private final UserTableRepository userTableRepository;
    private final MealBookingTableRepository mealBookingTableRepository;
    private final NotificationRepository notificationRepository;
    private final MealBookingLogRepository mealBookingLogRepository;


    public UserDto getUserById(Long userId) {
        UserTable user = userTableRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        List<MealBookingTable> allBookings = mealBookingTableRepository.findByUserID(user);

        // Filter bookings that are older than today
        LocalDate today = LocalDate.now();
        for (MealBookingTable booking : allBookings) {
            if (booking.getBookingDate().isBefore(today)) {

                // Create notification
                var notification = NotificationTable.builder()
                        .userID(user)
                        .role(user.getRole().name())
                        .message("For UserID " + userId + " " + user.getFirstName() + " unused coupon Expired " + booking.getBookingDate())
                        .unread(true)
                        .build();
                notificationRepository.save(notification);

                // Expire the booking
                var expiredCoupon = MealBookingLog.builder()
                        .mealBookingID(booking.getBookingID())
                        .status(MealBookingLog.Status.EXPIRED)  // Change status to EXPIRED
                        .build();
                mealBookingLogRepository.save(expiredCoupon);

                // Delete the expired booking
                mealBookingTableRepository.delete(booking);
            }
        }



        return UserDto.builder()
                .User_ID(user.getUserID())
                .user_email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().name())
                .build();
    }





}
