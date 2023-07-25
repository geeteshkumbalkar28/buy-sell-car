package com.spring.jwt.dto.BookingDtos;

import lombok.Data;

@Data
public class DeleteResponseDto {
    private String message;
    private String exception;

    public DeleteResponseDto(String message) {
        this.message = message;
    }
}
