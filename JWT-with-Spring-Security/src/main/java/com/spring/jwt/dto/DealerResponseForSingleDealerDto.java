package com.spring.jwt.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DealerResponseForSingleDealerDto {
    private String message;
    private DealerDto dealerDto;
    private String exception;

    public DealerResponseForSingleDealerDto(String message) {
        this.message = message;
    }
}
