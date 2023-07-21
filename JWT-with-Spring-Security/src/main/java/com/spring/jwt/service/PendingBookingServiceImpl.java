package com.spring.jwt.service;

import com.spring.jwt.dto.CarDto;
import com.spring.jwt.dto.DealerDto;
import com.spring.jwt.dto.PendingBookingDTO;
import com.spring.jwt.entity.Car;
import com.spring.jwt.entity.Dealer;
import com.spring.jwt.entity.PendingBooking;
import com.spring.jwt.repository.CarRepo;
import com.spring.jwt.repository.PendingBookingRepository;
import com.spring.jwt.Interfaces.PendingBookingService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
    public class PendingBookingServiceImpl implements PendingBookingService {

        private final PendingBookingRepository pendingBookingRepository;
        private final CarRepo carRepository;


    @Override
    public PendingBooking savePendingBooking(PendingBookingDTO pendingBookingDTO) {
        PendingBooking pendingBooking = mapToPendingBooking(pendingBookingDTO);
        return pendingBookingRepository.save(pendingBooking);
    }

    private PendingBooking mapToPendingBooking(PendingBookingDTO pendingBookingDTO) {
        Optional<Car> optionalCar = carRepository.findById(pendingBookingDTO.getCarId());
        Car car = optionalCar.orElseThrow(() -> new EntityNotFoundException("Car not found"));

        // Ensure dealerId is not null
        int dealerId = Objects.requireNonNullElse(pendingBookingDTO.getDealerId(), -1);

        PendingBooking pendingBooking = new PendingBooking();
        pendingBooking.setDate(pendingBookingDTO.getDate());
        pendingBooking.setPrice(pendingBookingDTO.getPrice());
        pendingBooking.setStatus(pendingBookingDTO.getStatus());
        pendingBooking.setUserId(pendingBookingDTO.getUserId());
        pendingBooking.setDealerId(dealerId); // Use the default value (-1) if dealerId is null
        pendingBooking.setAskingPrice(pendingBookingDTO.getAskingPrice());
        pendingBooking.setCarCar(car);
        return pendingBooking;
    }

    // Other methods for mapping CarDto and DealerDto to Car and Dealer entities...

    private Car mapToCar(CarDto carDto) {
        return new Car(carDto);
    }

    private Dealer mapToDealer(DealerDto dealerDto) {
        return new Dealer(dealerDto);
    }

}
