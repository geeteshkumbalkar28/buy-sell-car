package com.spring.jwt.Interfaces;

import com.spring.jwt.dto.BookingDtos.PendingBookingRequestDto;
import com.spring.jwt.dto.PendingBookingDTO;
import com.spring.jwt.entity.PendingBooking;

import java.util.List;

public interface PendingBookingService {

    public PendingBookingRequestDto savePendingBooking(PendingBookingDTO pendingBookingDTO);


    public void deleteBooking(int id);

    public void statusUpdate(PendingBookingDTO pendingBookingDTO);

    public List<PendingBookingDTO> getAllPendingBookingWithPage(int PageNo);


    public List<com.spring.jwt.dto.BookingDtos.PendingBookingDTO>getAllPendingBookingByUserId(int pageNo, int userId);
    public com.spring.jwt.dto.BookingDtos.PendingBookingDTO getPendingBookingId(int bookingId);

    public List<com.spring.jwt.dto.BookingDtos.PendingBookingDTO> getPendingBookingsByDealerId(int pageNo, int dealerId);

    public List<com.spring.jwt.dto.BookingDtos.PendingBookingDTO> getPendingBookingsByCarId(int pageNo, int carId);
}
