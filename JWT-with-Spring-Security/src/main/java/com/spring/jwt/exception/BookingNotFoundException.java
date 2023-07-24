package com.spring.jwt.exception;

import java.util.function.Supplier;

public class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException(String message) {
        super(message);
    }
}
