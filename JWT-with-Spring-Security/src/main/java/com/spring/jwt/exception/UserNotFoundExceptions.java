package com.spring.jwt.exception;

<<<<<<< HEAD
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

=======
public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message) {
        super(message);
>>>>>>> 0d301b77b8607abadd3a61f0d5eab98c3f0a694c
    }
}
