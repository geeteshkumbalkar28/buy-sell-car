package com.spring.jwt.exception;

import org.springframework.http.HttpStatus;

public class NoImageFoundException extends RuntimeException{
    public NoImageFoundException(String imageNotFound, HttpStatus httpStatus) {
    }

    public NoImageFoundException() {

    }
}
