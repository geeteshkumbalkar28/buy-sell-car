package com.spring.jwt.dto.DealerResponseDtos;

import lombok.Data;

@Data
public class DealerStatusDto {
    private String statusMessage;
    private String message;
    private String exception;

    public DealerStatusDto(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
