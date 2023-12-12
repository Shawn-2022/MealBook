package com.javaProject.mealBookingProject.customExceptions;

public class TokenGenerationException extends RuntimeException {
    public TokenGenerationException(String message, Exception e) {
        super(message);
    }
}

