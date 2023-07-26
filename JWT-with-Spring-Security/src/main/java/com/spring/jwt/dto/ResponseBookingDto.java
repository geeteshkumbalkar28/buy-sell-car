package com.spring.jwt.dto;

import lombok.Data;

@Data
public class ResponseBookingDto {

    private String message;
    private String exception;

    public ResponseBookingDto(String message) {

        this.message =message;
    }
}
