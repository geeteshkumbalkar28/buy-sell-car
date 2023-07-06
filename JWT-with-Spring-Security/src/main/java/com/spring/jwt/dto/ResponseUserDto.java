package com.spring.jwt.dto;

import com.spring.jwt.entity.User;
import com.spring.jwt.entity.Userprofile;
import lombok.Data;

@Data
public class ResponseUserDto {

    private String mobile_no;

    private String address;

    private String email;

    private String city;

    private String firstName;

    private String lastName;


    public ResponseUserDto(Userprofile userprofile, User user) {
        this.mobile_no = user.getMobileNo();
        this.address = userprofile.getAddress();
        this.email = user.getEmail();
        this.city = userprofile.getCity();
        this.firstName = userprofile.getFirstName();
        this.lastName = userprofile.getLastName();
    }

    public ResponseUserDto() {

    }
}
