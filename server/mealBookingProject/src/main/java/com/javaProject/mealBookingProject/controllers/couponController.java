package com.javaProject.mealBookingProject.controllers;

import com.javaProject.mealBookingProject.customExceptions.UserNotFoundException;
import com.javaProject.mealBookingProject.dto.redeemDto;
import com.javaProject.mealBookingProject.service.MealBookingService;
import com.javaProject.mealBookingProject.service.couponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("mealBooking/meal")
public class couponController {

    @Autowired
    private couponService couponService;

    //shyam
    //redeem
    @GetMapping("/Coupon/{bookingDate}")
    public ResponseEntity<redeemDto> redeemBooking(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate bookingDate)
    {
        {
            try {
                redeemDto bookingDto = couponService.getBookingByUserAndDate(bookingDate);
                return ResponseEntity.ok(bookingDto);
            } catch (RuntimeException e) {
                // Handle the exception or log an error
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }
    }


    //nishi
    @DeleteMapping("/redeemedCoupon/{bookingId}")
    public ResponseEntity<String> redeemedMealBooking(@PathVariable Long bookingId) {
        try {
            ResponseEntity<String> booking = couponService.getRedeemConfirmation(bookingId);
            return booking;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while redeeming the booking."+e);
        }
    }


    //nishi
    @DeleteMapping("/expireCoupon/{bookingId}")
    public ResponseEntity<String> expireBooking(@PathVariable Long bookingId) {
        try {
            ResponseEntity<String> result = couponService.expireCouponIfApplicable(bookingId);
            return result;
        } catch (UserNotFoundException e) {
            // Log the error using a logging framework
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        } catch (Exception e) {
            // Log the error using a logging framework
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while expiring the booking coupon. " + e.getMessage());
        }
    }


}
