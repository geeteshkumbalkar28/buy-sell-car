package com.spring.jwt.exception;

import org.springframework.http.HttpStatus;

public class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException(String message, HttpStatus notFound) {
        super(message);
    }

    public BookingNotFoundException(String message) {
        super(message);
    }
}
