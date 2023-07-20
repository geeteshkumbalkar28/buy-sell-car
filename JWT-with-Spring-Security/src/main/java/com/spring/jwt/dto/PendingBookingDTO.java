package com.spring.jwt.dto;

import com.spring.jwt.entity.Status;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PendingBookingDTO {

    public static final String STATUS_PENDING = "Pending";
    public static final String STATUS_ACTIVE = "Active";
    public static final String STATUS_SOLD = "Sold";

    private LocalDate date;
    private int price;
    private int askingPrice;
    private Status status;
    private Integer carId;
    private CarDto carDto;

}
