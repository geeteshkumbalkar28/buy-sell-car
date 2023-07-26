package com.spring.jwt.exception;

import org.springframework.http.HttpStatus;

public class BookingException extends RuntimeException{

    private String message;

    private HttpStatus httpStatus;

    public BookingException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }


    public String getMessage() {
        return message;
    }
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
