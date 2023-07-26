package com.spring.jwt.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Integer id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "price", length = 45)
    private Integer price;

    @Column(name = "userId", length = 45)
    private Integer userId;

    @Column(name = "dealerId", length = 45)
    private Integer dealerId;

    @Column(name = "carId", length = 45)
    private Integer carId;

//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "pending_booking")
//    private PendingBooking pendingBooking;

}