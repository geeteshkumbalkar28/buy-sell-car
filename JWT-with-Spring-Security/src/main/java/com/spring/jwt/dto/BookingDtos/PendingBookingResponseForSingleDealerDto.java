package com.spring.jwt.dto.BookingDtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PendingBookingResponseForSingleDealerDto {
    private String message;
    private PendingBookingDTO pendingBookingDTO;
    private String exception;

    public PendingBookingResponseForSingleDealerDto(String message) {
        this.message = message;
    }
}
