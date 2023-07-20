package com.spring.jwt.dto;


import lombok.Data;

@Data
public class ResponceDto {
    public String message;
    public Object object;


    public ResponceDto(String message, Object object) {
        this.message = message;
        this.object = object;
    }
}
