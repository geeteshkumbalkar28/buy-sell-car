package com.spring.jwt.exception;

public class UserNotDealerException extends RuntimeException{
    public UserNotDealerException(String message) {
        super(message);
    }
}
