package com.spring.jwt.repository;

import com.spring.jwt.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    Optional<Booking> findByUserId(int userId);

    List<Booking> findByDealerId(int dealerId);
}
