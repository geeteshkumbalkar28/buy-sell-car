package com.spring.jwt.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class ResponseDto {
    public String status;
    public String message;

    public ResponseDto(String status, String message) {
        this.status=status;
        this.message=message;
    }

    public ResponseDto() {

    }
}
