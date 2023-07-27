package com.spring.jwt.dto;

import com.spring.jwt.entity.Booking;
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

        public BookingDto(Booking booking, Booking booking1) {
        }

        public BookingDto() {

        }



        public BookingDto(Booking booking) {
                this.id = booking.getId();
                this.date = booking.getDate();
                this.price = booking.getPrice();
                this.userId = booking.getUserId();
                this.dealerId = booking.getDealerId();
                this.carId = booking.getCarId();
        }
}
