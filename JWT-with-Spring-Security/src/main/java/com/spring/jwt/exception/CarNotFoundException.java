package com.spring.jwt.exception;

public class CarNotFoundException extends RuntimeException{
    public CarNotFoundException() {
        super("car not found");
    }

    public CarNotFoundException(String message) {

        super(message);
    }
}
