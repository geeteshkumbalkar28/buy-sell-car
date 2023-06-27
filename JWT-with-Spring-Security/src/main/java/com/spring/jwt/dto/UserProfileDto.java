package com.spring.jwt.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileDto {

    public String password;

    private String mobile_no;

    private String address;

    private String email;


    private String city;


    private String firstName;


    private String lastName;

}
