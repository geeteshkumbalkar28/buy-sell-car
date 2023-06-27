package com.spring.jwt.dto;


import com.spring.jwt.entity.Userprofile;
import lombok.ToString;

@ToString
public class ResponseUserProfileDto {
    public String address;

    public String city;

    public String fristname;

    public String lastName;

    public ResponseUserProfileDto(Userprofile userprofile) {
        this.address = userprofile.getAddress();
        this.city = userprofile.getCity();
        this.fristname = userprofile.getFirstName();
        this.lastName =userprofile.getLastName();

    }

}

