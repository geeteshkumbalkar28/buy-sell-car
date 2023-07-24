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

}
