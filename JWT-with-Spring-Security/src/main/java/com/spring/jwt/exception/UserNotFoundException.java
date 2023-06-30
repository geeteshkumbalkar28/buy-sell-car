package com.spring.jwt.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends RuntimeException{

    private HttpStatus httpStatus;


    public UserNotFoundException() {
    }

    public UserNotFoundException(String s) {
        super(s);
    }

    public UserNotFoundException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;

    }
}
