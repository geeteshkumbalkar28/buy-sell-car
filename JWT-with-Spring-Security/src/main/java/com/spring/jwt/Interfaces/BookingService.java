package com.spring.jwt.Interfaces;

import com.spring.jwt.dto.BookingDto;

import java.util.List;

public interface BookingService {

    BookingDto saveBooking(BookingDto bookingDTO);


    List<BookingDto> getAllBooking(int pageNo);

    BookingDto getAllBookingsByUserId(int userId);

    List<BookingDto> getAllBookingsByDealerId(int dealerId,int pageNo);

    BookingDto getBookingById(int id);
}
