package com.javaProject.mealBookingProject.controllers;

import com.javaProject.mealBookingProject.customExceptions.UserNotFoundException;
import com.javaProject.mealBookingProject.dto.cancelBookingDto;
import com.javaProject.mealBookingProject.dto.createBookingRequestDto;
import com.javaProject.mealBookingProject.dto.redeemDto;
import com.javaProject.mealBookingProject.dto.viewBookingDto;
import com.javaProject.mealBookingProject.service.MealBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

//nishi
@RestController
@RequestMapping("mealBooking/meal")
public class MealBookingController {

    @Autowired
    private MealBookingService mealBookingService;

    @PostMapping("/createbooking")
    public String createMealBookings(@RequestBody createBookingRequestDto createBookingRequestDTO) {
        try {
            ResponseEntity<String> responseEntity = mealBookingService.createMealBooking(createBookingRequestDTO);
            return responseEntity.getBody();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating meal bookings: " + e.getMessage(), e);
        }
    }
    // Other endpoints for updating, retrieving, or deleting meal bookings


    //delete api
    @DeleteMapping("/cancelbooking")
    public ResponseEntity<String> cancelMealBooking(@RequestBody cancelBookingDto cancelDto)
    {
        try {

            // Delegate the cancellation logic to the service
            mealBookingService.cancelMealBooking(cancelDto);
            return ResponseEntity.ok("Booking canceled successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while canceling the booking."+e);
        }
    }

    //get api
    @GetMapping("/getBooking")
    public ResponseEntity<List<viewBookingDto>> getBooking() {
        List<viewBookingDto> bookingHistory = mealBookingService.getBooking();
        return new ResponseEntity<>(bookingHistory, HttpStatus.OK);
    }

}