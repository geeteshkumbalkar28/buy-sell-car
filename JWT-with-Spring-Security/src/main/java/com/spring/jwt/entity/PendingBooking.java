package com.spring.jwt.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "pending_booking")
public class PendingBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "price", length = 45)
    private int price;

    @Column(name = "dealerId", length = 45)
    private Integer dealerId;

    @Column(name = "userId", length = 45)
    private Integer userId;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column (name = "asking_price", nullable = false)
    private int askingPrice;

    @ManyToOne
    @JoinColumn(name = "car_car_id")
    @JsonIgnore
    private Car carCar;

    @OneToMany(mappedBy = "pendingBooking")
    private List<Booking> bookings;


}