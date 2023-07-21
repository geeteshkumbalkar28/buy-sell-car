package com.spring.jwt.repository;

import com.spring.jwt.entity.PendingBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PendingBookingRepository extends JpaRepository<PendingBooking, Integer> {

}

