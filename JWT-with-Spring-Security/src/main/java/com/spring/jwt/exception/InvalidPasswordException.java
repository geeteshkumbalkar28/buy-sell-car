package com.spring.jwt.exception;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() {
    }

    public InvalidPasswordException(String invalidPassword) {
        super(invalidPassword);
    }
}
