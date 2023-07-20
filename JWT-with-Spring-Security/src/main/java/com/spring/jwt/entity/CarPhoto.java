package com.spring.jwt.entity;


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
@Table(name = "carphoto")
public class CarPhoto {
    @Id
    @Column(name = "car_photo_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //@OneToOne(cascade = CascadeType.ALL)
   // @JoinColumn(name = "Car_Car_Id")
    @Column(name = "CarId", nullable = false)
    private int car_id;

    @Column(name = "PhotoLink", nullable = false)
    private String photo_link;

    @Column(name = "PhotoType", nullable = false)
    private String type;
}