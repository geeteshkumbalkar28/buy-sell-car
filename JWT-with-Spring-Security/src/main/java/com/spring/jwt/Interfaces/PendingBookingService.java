package com.spring.jwt.Interfaces;

import com.spring.jwt.dto.PendingBookingDTO;
import com.spring.jwt.entity.PendingBooking;

import java.util.List;

public interface PendingBookingService {

    PendingBooking savePendingBooking(PendingBookingDTO pendingBookingDTO);


    void deleteBooking(int id);

    void statusUpdate(PendingBookingDTO pendingBookingDTO);

   List<PendingBookingDTO> getAllPendingBookingWithPage(int PageNo);

   List<PendingBookingDTO>getAllPendingBookingByUserIdWithPage(int PageNo);

    public com.spring.jwt.dto.BookingDtos.PendingBookingDTO getPendingBookingId(int bookingId);

    public List<com.spring.jwt.dto.BookingDtos.PendingBookingDTO> getPendingBookingsByDealerId(int pageNo,int dealerId);

    public List<com.spring.jwt.dto.BookingDtos.PendingBookingDTO> getPendingBookingsByCarId(int pageNo, int carId);
}
