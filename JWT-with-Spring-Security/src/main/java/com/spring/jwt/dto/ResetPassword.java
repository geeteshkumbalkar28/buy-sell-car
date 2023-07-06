package com.spring.jwt.dto;

import lombok.Data;

@Data
public class ResetPassword {

    private  String token;

    private String password;

}


