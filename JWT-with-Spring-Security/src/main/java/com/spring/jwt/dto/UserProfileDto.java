package com.spring.jwt.dto;


import com.spring.jwt.entity.User;
import com.spring.jwt.entity.Userprofile;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileDto {

    private int id;

    private String mobile_no;

    private String address;

    private String email;

    private String city;

    private String firstName;

    private String lastName;

    public UserProfileDto(Userprofile userprofile, User user){
        this.id=userprofile.getId();
        this.address=userprofile.getAddress();
        this.city=userprofile.getCity();
        this.firstName= userprofile.getFirstName();
        this.lastName=userprofile.getLastName();
        this.email=user.getEmail();
        this.mobile_no=user.getMobileNo();
    }

}
