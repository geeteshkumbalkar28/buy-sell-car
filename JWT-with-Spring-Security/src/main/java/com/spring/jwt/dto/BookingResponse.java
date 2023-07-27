package com.spring.jwt.dto;

import lombok.Data;

@Data
public class BookingResponse {
    private String message;

    private BookingDto bookingDto;

    private String exception;

    public BookingResponse(String message) {
        this.message = message;
    }
}
