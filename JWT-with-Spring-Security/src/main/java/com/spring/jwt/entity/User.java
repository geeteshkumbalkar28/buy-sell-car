package com.spring.jwt.entity;

import com.spring.jwt.dto.RegisterDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Set;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @Column(name = "user_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "user_id_generator")
    @SequenceGenerator(name = "user_id_generator", initialValue = 1000)
    private Integer id;


    @Column(name = "email", nullable = false, length = 250)
    private String email;

    @Column(name = "mobile_no")
    private String mobileNo;

    @Column(name = "password", nullable = false, length = 250)
    private String password;


    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Userprofile profile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Dealer dealers;


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id"))
    private Set<Role> roles;

    public User(RegisterDto registerDto) {
        this.email = registerDto.getEmail();
        this.mobileNo = registerDto.getMobileNo();
        this.password = registerDto.getPassword();
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}




