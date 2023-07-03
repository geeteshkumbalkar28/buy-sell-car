package com.spring.jwt.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseSingleCarDto {
    private String message;
    private Object object;
    private String exception;

    public ResponseSingleCarDto(String message) {
        this.message = message;
    }
}
