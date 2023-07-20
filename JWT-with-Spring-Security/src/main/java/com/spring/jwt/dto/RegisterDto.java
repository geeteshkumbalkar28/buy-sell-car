package com.spring.jwt.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterDto {


    public String email;
    private String password;
    public String mobileNo;
    public String firstName;
    public String lastName;
    public String address;
    public String city;
    public String roles;
    public int document;
    public String shopName;
    public String area;
    public boolean status;
    public String userType;

}
