package com.spring.jwt.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundExceptions extends RuntimeException{

    private HttpStatus httpStatus;


    public UserNotFoundExceptions() {
    }

    public UserNotFoundExceptions(String s) {
        super(s);
    }

    public UserNotFoundExceptions(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;

    }}
