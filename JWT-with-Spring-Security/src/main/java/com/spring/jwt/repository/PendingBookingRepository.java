package com.spring.jwt.repository;

import com.spring.jwt.entity.PendingBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PendingBookingRepository extends JpaRepository<PendingBooking, Integer> {
        public Optional<List<PendingBooking>> findByDealerId(int dealerId);

        void deleteByCarCarId(Integer carId);

        List<PendingBooking> findByCarCarId(Integer carId);
        public Optional<List<PendingBooking>> getAllPendingBookingByUserId(int userId);
}

