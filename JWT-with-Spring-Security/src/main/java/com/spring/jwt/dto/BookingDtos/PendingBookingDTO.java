package com.spring.jwt.dto.BookingDtos;

import com.spring.jwt.entity.PendingBooking;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PendingBookingDTO {


    private int pendingBookingId;
    private LocalDate date;
    private int price;
    private Integer dealerId;
    private Integer carId;
    private Integer userId;
    private String status;
    private int askingPrice;
    public PendingBookingDTO() {
    }
    public PendingBookingDTO(PendingBooking pendingBooking) {
        this.pendingBookingId = pendingBooking.getId();
        this.carId = pendingBooking.getCarCar().getId();
        this.date = pendingBooking.getDate();
        this.price = pendingBooking.getPrice();
        this.userId = pendingBooking.getUserId();
        this.status = String.valueOf(pendingBooking.getStatus());
        this.askingPrice = pendingBooking.getAskingPrice();
        this.dealerId = pendingBooking.getDealerId();
    }
}
