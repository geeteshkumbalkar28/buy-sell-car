package com.spring.jwt.entity;

import com.spring.jwt.dto.RegisterDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "userprofile")
public class Userprofile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_profile_id", nullable = false)
    private int id;

    @Column(name = "address")
    private String address;

    @Column(name = "city", nullable = false, length = 45)
    private String city;

    @Column(name = "firstName", nullable = false, length = 45)
    private String firstName;

    @Column(name = "last_name", length = 45)
    private String lastName;

    @OneToOne
    @JoinColumn(name = "UserId")
    private User user;

    public Userprofile(RegisterDto registerDto) {
        this.address = registerDto.getAddress();
        this.city = registerDto.getCity();
        this.firstName = registerDto.getFirstName();
        this.lastName = registerDto.getLastName();
    }

    @Override
    public String toString() {
        return "Userprofile{" +
                "address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", fristName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}