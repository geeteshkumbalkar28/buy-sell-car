package com.spring.jwt.controller;


import com.spring.jwt.dto.PendingBookingDTO;
import com.spring.jwt.entity.Car;
import com.spring.jwt.entity.Dealer;
import com.spring.jwt.entity.PendingBooking;
import com.spring.jwt.entity.Status;
import com.spring.jwt.exception.DealerNotFoundException;
import com.spring.jwt.repository.CarRepo;
import com.spring.jwt.repository.DealerRepository;
import com.spring.jwt.service.PendingBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/booking")
public class PendingBookingController {

    private final CarRepo carRepo;

    private final DealerRepository dealerRepository;

    private final PendingBookingService pendingBookingService;


    @PostMapping("/request")
    public ResponseEntity<String> requestCarBooking(@RequestBody PendingBookingDTO pendingBookingDTO) {

        int carId = pendingBookingDTO.getCarId();
        Optional<Car> optionalCar = carRepo.findById(carId);

        if (optionalCar.isPresent()) {
            Car car = optionalCar.get();

            pendingBookingDTO.setDate(LocalDate.now());
            pendingBookingDTO.setPrice(car.getPrice());
            pendingBookingDTO.setStatus(pendingBookingDTO.getStatus());

            car.setCarStatus(pendingBookingDTO.getStatus());

            int dealerId = car.getDealerId();
            Dealer dealer = dealerRepository.findById(dealerId).orElseThrow(() -> new DealerNotFoundException("Dealer not found"));;

            if (dealer != null) {
                pendingBookingDTO.setAskingPrice(pendingBookingDTO.getAskingPrice());

                PendingBooking savePendingBooking = pendingBookingService.savePendingBooking(pendingBookingDTO);

                carRepo.save(car);

                return ResponseEntity.ok("Car booking request is pending.");
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
}

}


