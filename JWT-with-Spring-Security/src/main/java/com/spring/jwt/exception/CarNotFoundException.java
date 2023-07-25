package com.spring.jwt.exception;

import org.springframework.http.HttpStatus;

public class CarNotFoundException extends RuntimeException{
    private HttpStatus httpStatus;
    public CarNotFoundException() {


        super("car not found");
    }

    public CarNotFoundException(String message, HttpStatus httpStatus) {

        super(message);
        this.httpStatus = httpStatus;

    }

    public CarNotFoundException(String s) {
        super(s);
    }
}
