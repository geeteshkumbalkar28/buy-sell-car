package com.spring.jwt.dto;

import com.spring.jwt.entity.Booking;
import lombok.Data;

import java.util.List;

@Data
public class ResponseAllBookingDto {

    private String status;

    private String message;

    private List<BookingDto> bookings;

    private String exception;

    public ResponseAllBookingDto(String status) {
        this.status = status;
    }
}
