package com.javaProject.mealBookingProject.service;

import com.javaProject.mealBookingProject.customExceptions.InvalidDateRangeException;
import com.javaProject.mealBookingProject.customExceptions.UserNotFoundException;
import com.javaProject.mealBookingProject.dto.cancelBookingDto;
import com.javaProject.mealBookingProject.dto.createBookingRequestDto;
import com.javaProject.mealBookingProject.dto.redeemDto;
import com.javaProject.mealBookingProject.dto.viewBookingDto;
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
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MealBookingService {

    private final MealBookingTableRepository mealBookingTableRepository;
    private final UserTableRepository userTableRepository;
    private final MealBookingLogRepository mealBookingLogRepository;
    private final NotificationRepository notificationRepository;

    @Autowired
    private HttpServletRequest request;


    //create-booking api

    public ResponseEntity<String> createMealBooking(createBookingRequestDto createBookingRequestDTO) {
        Long userId = (Long) request.getAttribute("userId");

//        List<MealBookingTable> mealBookings = new ArrayList<>();

        try {
            LocalDate startDate = LocalDate.parse(createBookingRequestDTO.getStartDate());
            LocalDate endDate = LocalDate.parse(createBookingRequestDTO.getEndDate());
            String qrCodeContent;

            List<LocalDate> dateRange = generateDateRange(startDate, endDate);
            UserTable user = userTableRepository.findById(userId).orElseThrow(() ->new UserNotFoundException("User not found with email: " + userId));
            int bookingsCount = 0;

            for (LocalDate date : dateRange) {
                String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                qrCodeContent = "USERID" + user.getUserID() + "-" + formattedDate;
                var mealBooking = MealBookingTable.builder()
                        .userID(user)
                        .bookingDate(date)
                        .qrCode(qrCodeContent)
                        .build();
                mealBookingTableRepository.save(mealBooking);
                var mealBookingLog = MealBookingLog.builder()
                        .mealBookingID(mealBooking.getBookingID())
                        .status(MealBookingLog.Status.BOOKED)
                        .build();
                mealBookingLogRepository.save(mealBookingLog);
                var notification = NotificationTable.builder()
                        .userID(user)
                        .role(user.getRole().name())
                        .message("For UserID " + userId + " " + user.getFirstName() + " booking has been successfully on " + date)
                        .unread(true)
                        .build();
                notificationRepository.save(notification);
                bookingsCount++;
            }

            String responseMessage = "Bookings created successfully for " + bookingsCount + " dates";
            return ResponseEntity.ok(responseMessage);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid date format or null date provided"+e);
        }
    }

    //booking cancel api
    public void cancelMealBooking(cancelBookingDto cancelDto) throws BadRequestException {
        try {
            // Validation
            Long userID = (Long) request.getAttribute("userId");
            LocalDate cancelDate = cancelDto.getCancelDate();
            if (cancelDate == null || userID == null) {
                throw new BadRequestException("Invalid request. Data is missing.");
            }

            // User Existence
            UserTable user = userTableRepository.findById(userID)
                    .orElseThrow(()  ->new UserNotFoundException("User not found with email: " + userID));

            // Find Booking for User
            List<MealBookingTable> userBookings = mealBookingTableRepository.findByUserID(user);

            // Filter Booking by Cancel Date
            Optional<MealBookingTable> bookingForCancelDate = userBookings.stream()
                    .filter(booking -> booking.getBookingDate().equals(cancelDate))
                    .findFirst();

            if (bookingForCancelDate.isPresent()) {
                // Booking found for the specified date and user
                 MealBookingTable cancelBooking = bookingForCancelDate.get();

                // Cancellation Logic
                var log = MealBookingLog.builder()
                        .mealBookingID(cancelBooking.getBookingID())
                        .status(MealBookingLog.Status.CANCELLED)
                        .build();

                mealBookingLogRepository.save(log);
                mealBookingTableRepository.delete(cancelBooking);
                var notification = NotificationTable.builder()
                        .userID(user)
                        .role(user.getRole().name())
                        .message("For UserID " +userID+" "+ user.getFirstName()+" booking has been successfully cancel for "+cancelDate)
                        .unread(true)
                        .build();
                notificationRepository.save(notification);
                // If needed, you can return a response to the client indicating successful cancellation
            } else {
                // No booking found for the specified date and user
                // Depending on your requirements, you can return a response to the client or throw an exception
                throw new ChangeSetPersister.NotFoundException();
            }


        } catch (Exception e) {
            // Handle the exception or log an error
            e.printStackTrace();  // Print the stack trace for debugging
        }
    }

    //get bookings
    public List<viewBookingDto> getBooking() {
        Long userId = (Long) request.getAttribute("userId");

        try {
            UserTable user = userTableRepository.findById(userId)
                    .orElseThrow(()  ->new UserNotFoundException("User not found with email: " + userId));

            List<MealBookingTable> userBookings = mealBookingTableRepository.findByUserID(user);

            return userBookings.stream()
                    .map(booking -> new viewBookingDto(booking.getBookingID(), booking.getBookingDate()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            // Handle the exception or log an error
            e.printStackTrace(  );  // Print the stack trace for debugging
            throw new RuntimeException("Failed to fetch booking history");
        }
    }
    // method to set it as expired / auto expire


    //booking helpers
    // Existing method to check if a given LocalDate is a weekday
    private static boolean isWeekday(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
    }
    private static boolean isExcludedDate(LocalDate date) {
        // Exclude specific dates: 25 December, 31 December, 15 January, 26 January
        return (date.getMonth() == Month.DECEMBER && (date.getDayOfMonth() == 25 || date.getDayOfMonth() == 31))
                || (date.getMonth() == Month.JANUARY && (date.getDayOfMonth() == 15 || date.getDayOfMonth() == 26));
    }
    public static List<LocalDate> generateDateRange(LocalDate startDate, LocalDate endDate) throws InvalidDateRangeException {
        LocalDate todayPlusOne = LocalDate.now().plusDays(1);

        if (startDate.isBefore(todayPlusOne)) {
            throw new InvalidDateRangeException("Start date should not be before today + 1");
        }

        if (ChronoUnit.DAYS.between(startDate, endDate) > 90) {
            throw new InvalidDateRangeException("Date range cannot exceed 90 days");
        }

        List<LocalDate> dateRange = new ArrayList<>();
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            if (isWeekday(currentDate) && !isExcludedDate(currentDate)) {
                dateRange.add(currentDate);
            }
            currentDate = currentDate.plus(1, ChronoUnit.DAYS);
        }
        return dateRange;
    }









}
