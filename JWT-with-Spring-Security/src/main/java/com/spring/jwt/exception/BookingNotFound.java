package com.spring.jwt.exception;

import org.springframework.http.HttpStatus;

public class BookingNotFound extends RuntimeException {
    public BookingNotFound(String bookingNotFound, HttpStatus notFound) {
    }
}
