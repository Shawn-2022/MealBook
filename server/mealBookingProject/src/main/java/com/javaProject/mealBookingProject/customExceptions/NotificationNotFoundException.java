package com.javaProject.mealBookingProject.customExceptions;
public class NotificationNotFoundException extends RuntimeException {

    public NotificationNotFoundException(String message) {
        super(message);
    }
}

