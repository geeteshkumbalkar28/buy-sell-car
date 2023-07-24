package com.spring.jwt.dto;

import lombok.Data;

import java.util.List;

@Data

public class ResponseAllPendingBookingDto {
    private String message;
    private List<PendingBookingDTO> list;
    private String exception;

    public ResponseAllPendingBookingDto(String message){
        this.message=message;
    }

}
