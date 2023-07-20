package com.spring.jwt.service;

import com.spring.jwt.dto.PendingBookingDTO;
import com.spring.jwt.entity.PendingBooking;

public interface PendingBookingService {

    PendingBooking savePendingBooking(PendingBookingDTO pendingBookingDTO);

}
