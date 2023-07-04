package com.spring.jwt.exception;

public class DealerNotFoundException extends RuntimeException{
    public DealerNotFoundException(String message) {
        super(message);
    }
}
