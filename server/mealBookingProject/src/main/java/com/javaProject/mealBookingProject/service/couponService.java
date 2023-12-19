package com.javaProject.mealBookingProject.service;

import com.javaProject.mealBookingProject.customExceptions.UserNotFoundException;
import com.javaProject.mealBookingProject.dto.redeemDto;
import com.javaProject.mealBookingProject.entity.MealBookingLog;
import com.javaProject.mealBookingProject.entity.MealBookingTable;
import com.javaProject.mealBookingProject.entity.NotificationTable;
import com.javaProject.mealBookingProject.entity.UserTable;
import com.javaProject.mealBookingProject.repository.MealBookingLogRepository;
import com.javaProject.mealBookingProject.repository.MealBookingTableRepository;
import com.javaProject.mealBookingProject.repository.NotificationRepository;
import com.javaProject.mealBookingProject.repository.UserTableRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class couponService {

    @Autowired
    private final MealBookingTableRepository mealBookingTableRepository;
    @Autowired
    private final UserTableRepository userTableRepository;
    @Autowired
    private final MealBookingLogRepository mealBookingLogRepository;
    @Autowired
    private final NotificationRepository notificationRepository;

    @Autowired
    private HttpServletRequest request;


    public redeemDto getBookingByUserAndDate(LocalDate bookingDate){
        Long userId = (Long) request.getAttribute("userId");
        try {

            // User Existence
            UserTable user = userTableRepository.findById(userId)
                    .orElseThrow(()  ->new UserNotFoundException("User not found with email: " + userId));

            // Find Booking for User
            List<MealBookingTable> booking = mealBookingTableRepository.findByUserID(user);

            // Filter Booking by booking Date
            Optional<MealBookingTable> bookingForDate = booking.stream()
                    .filter(meal -> meal.getBookingDate().equals(bookingDate))
                    .findFirst();

//            if (bookingForDate.isPresent()&& bookingForDate.get().getBookingDate().isEqual(LocalDate.now()) ) {
            if (bookingForDate.isPresent() ) {
                // Booking found for the specified date and user
                MealBookingTable mealCoupon = bookingForDate.get();
                var notification = NotificationTable.builder()
                        .userID(user)
                        .role(user.getRole().name())
                        .message("For UserID " + userId + " " + user.getFirstName() + " booking coupon generation successfully " + bookingDate)
                        .unread(true)
                        .build();
                notificationRepository.save(notification);
                var coupon = redeemDto.builder()
                        .userID(user.getUserID())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .Coupon(mealCoupon.getQrCode())
                        .bookingDate(mealCoupon.getBookingDate())
                        .bookingId(mealCoupon.getBookingID())
                        .build();
                return coupon;
            }
            else {
                return null;
            }
        } catch (Exception e) {
            // Handle the exception or log an error
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch booking for user and date");
        }
    }

    //redemption success
    public ResponseEntity<String> getRedeemConfirmation( Long bookingId) {
        Long userId = (Long) request.getAttribute("userId");
        try {
            // User Existence
            UserTable user = userTableRepository.findById(userId)
                    .orElseThrow(()  ->new UserNotFoundException("User not found with email: " + userId));

            // Find Booking by ID
            Optional<MealBookingTable> optionalBooking = mealBookingTableRepository.findById(bookingId);

            if (optionalBooking.isPresent()) {
                // Booking found for the specified ID
                MealBookingTable mealCoupon = optionalBooking.get();

                var notification = NotificationTable.builder()
                        .userID(user)
                        .role(user.getRole().name())
                        .message("For UserID " + userId + " " + user.getFirstName() + " booking coupon Redeemed successfully " + mealCoupon.getBookingDate())
                        .unread(true)
                        .build();
                notificationRepository.save(notification);

                var redeemed = MealBookingLog.builder()
                        .mealBookingID(mealCoupon.getBookingID())
                        .status(MealBookingLog.Status.REDEEMED)
                        .build();

                mealBookingLogRepository.save(redeemed);
                mealBookingTableRepository.delete(mealCoupon);
                return ResponseEntity.ok("Booking Redeemed successfully.");
            }
            else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No booking found for the specified date and user.");
            }
        } catch (Exception e) {
            // Handle the exception or log an error
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch booking for user and date");
        }

    }

    //redemption expired
    public ResponseEntity<String> expireCouponIfApplicable( Long bookingId) {
        Long userId = (Long) request.getAttribute("userId");
        try {
            // User Existence
            UserTable user = userTableRepository.findById(userId)
                    .orElseThrow(() ->new UserNotFoundException("User not found with email: " + userId));

            // Find Booking by ID
            Optional<MealBookingTable> optionalBooking = mealBookingTableRepository.findById(bookingId);

            if (optionalBooking.isPresent()) {
                // Booking found for the specified ID
                MealBookingTable mealCoupon = optionalBooking.get();
                ;

                var notification = NotificationTable.builder()
                        .userID(user)
                        .role(user.getRole().name())
                        .message("For UserID " + userId + " " + user.getFirstName() + " booking coupon Expired " + mealCoupon.getBookingDate())
                        .unread(true)
                        .build();
                notificationRepository.save(notification);

                var expiredCoupon = MealBookingLog.builder()
                        .mealBookingID(mealCoupon.getBookingID())
                        .status(MealBookingLog.Status.EXPIRED)  // Change status to EXPIRED
                        .build();

                mealBookingLogRepository.save(expiredCoupon);
                mealBookingTableRepository.delete(mealCoupon);
                return ResponseEntity.ok("Booking expired successfully.");
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No booking found for the specified date and user.");
            }
        } catch (Exception e) {
            // Handle the exception or log an error
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch booking for user and date");
        }
    }


}
