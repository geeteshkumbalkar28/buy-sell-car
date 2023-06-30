package com.spring.jwt.dto;

import lombok.Data;

import java.util.List;

@Data
public class ResponseAllCarDto {
    private String message;
    private List<CarDto> list;
    private String exception;

    public ResponseAllCarDto(String message){
        this.message=message;
    }
}
