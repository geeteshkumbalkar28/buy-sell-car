package com.spring.jwt.dto;

import lombok.ToString;
import org.springframework.http.ResponseEntity;
@ToString
public class ResponseDto {
    public String HttpMessage;
    public String message;

    public ResponseDto(String codeMessage, String message) {
        this.HttpMessage=codeMessage;
        this.message=message;
    }
}
