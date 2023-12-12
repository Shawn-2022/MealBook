package com.javaProject.mealBookingProject.customExceptions;
public class IncorrectOldPasswordException extends RuntimeException {
    public IncorrectOldPasswordException(String message) {
        super(message);
    }
}

