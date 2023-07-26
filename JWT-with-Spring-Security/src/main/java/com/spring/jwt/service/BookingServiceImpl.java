package com.spring.jwt.service;


import com.spring.jwt.Interfaces.BookingService;
import com.spring.jwt.Interfaces.ICarRegister;
import com.spring.jwt.dto.BookingDto;
import com.spring.jwt.dto.CarDto;
import com.spring.jwt.entity.Booking;
import com.spring.jwt.entity.Car;
import com.spring.jwt.entity.PendingBooking;
import com.spring.jwt.entity.Status;
import com.spring.jwt.exception.BookingException;
import com.spring.jwt.repository.BookingRepository;
import com.spring.jwt.repository.CarRepo;
import com.spring.jwt.repository.PendingBookingRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor

public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ICarRegister iCarRegister;
    private final PendingBookingRepository pendingBookingRepository;
    private final CarRepo carRepo;


    @Override
    public BookingDto saveBooking(BookingDto bookingDto) {
        CarDto car = iCarRegister.findById(bookingDto.getCarId());
        if (car == null) {
            throw new EntityNotFoundException("Car not found with ID: " + bookingDto.getCarId());
        }

        if (car.getCarStatus() == Status.SOLD) {
            throw new BookingException("Car is not available for booking as it's already sold.", HttpStatus.NOT_FOUND);
        }

        Booking booking = new Booking();
        BeanUtils.copyProperties(bookingDto, booking);
        booking = bookingRepository.save(booking);
        List<PendingBooking> pendingBookings = pendingBookingRepository.findByCarCarId(bookingDto.getCarId());
        if (!pendingBookings.isEmpty()) {
            Car carEntity = carRepo.findById(bookingDto.getCarId())
                    .orElseThrow(() -> new EntityNotFoundException("Car not found with ID: " + bookingDto.getCarId()));
            carEntity.setCarStatus(Status.SOLD);
            carRepo.save(carEntity);

            pendingBookingRepository.deleteAll(pendingBookings);
        }

        pendingBookingRepository.deleteByCarCarId(bookingDto.getCarId());
        BookingDto savedBookingDto = new BookingDto();
        BeanUtils.copyProperties(booking, savedBookingDto);
        return savedBookingDto;
    }

}

