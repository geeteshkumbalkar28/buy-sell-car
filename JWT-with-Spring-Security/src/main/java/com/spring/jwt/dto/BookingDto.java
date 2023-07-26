package com.spring.jwt.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingDto {

        private Integer id;
        private LocalDate date;
        private int price;
        private int carId;
        private int userId;
        private int dealerId;
        private int pendingBookingId;

}
