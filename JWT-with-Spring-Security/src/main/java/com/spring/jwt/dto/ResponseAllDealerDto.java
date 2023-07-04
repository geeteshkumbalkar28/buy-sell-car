package com.spring.jwt.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class ResponseAllDealerDto {
    private String message;
    private List<DealerDto> list;
    private String exception;

    public ResponseAllDealerDto(String message){
        this.message=message;
    }

}
