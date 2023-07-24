package com.spring.jwt.controller;


import com.spring.jwt.dto.*;
import com.spring.jwt.entity.Car;
import com.spring.jwt.entity.Dealer;
import com.spring.jwt.entity.PendingBooking;

import com.spring.jwt.entity.Status;
import com.spring.jwt.exception.BookingNotFound;


import com.spring.jwt.exception.CarNotFoundException;
import com.spring.jwt.exception.DealerNotFoundException;
import com.spring.jwt.exception.PageNotFoundException;
import com.spring.jwt.repository.CarRepo;
import com.spring.jwt.repository.DealerRepository;
import com.spring.jwt.Interfaces.PendingBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/booking")
public class PendingBookingController {

    private final CarRepo carRepo;

    private final DealerRepository dealerRepository;

    private final PendingBookingService pendingBookingService;

    @PostMapping("/request")
    public ResponseEntity<ResponceDto> requestCarBooking(@RequestBody PendingBookingDTO pendingBookingDTO) {
        int carId = pendingBookingDTO.getCarId();
        Optional<Car> optionalCar = carRepo.findById(carId);

        if (optionalCar.isPresent()) {
            Car car = optionalCar.get();

            pendingBookingDTO.setDate(LocalDate.now());
            pendingBookingDTO.setPrice(car.getPrice());
            pendingBookingDTO.setStatus(pendingBookingDTO.getStatus());
            pendingBookingDTO.setDealerId(car.getDealerId());
            car.setCarStatus(pendingBookingDTO.getStatus());

            int dealerId = car.getDealerId();
            Optional<Dealer> optionalDealer = dealerRepository.findById(dealerId);

            if (optionalDealer.isPresent()) {
                Dealer dealer = optionalDealer.get();
                pendingBookingDTO.setAskingPrice(pendingBookingDTO.getAskingPrice());

                PendingBooking savePendingBooking = pendingBookingService.savePendingBooking(pendingBookingDTO);

                carRepo.save(car);

                // Create a DTO for the response
                CarDto carDto = mapToCarDto(car, dealer);
                ResponceDto responseDto = new ResponceDto("Car booking request is pending.", carDto);
                return ResponseEntity.ok(responseDto);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private CarDto mapToCarDto(Car car, Dealer dealer) {
        CarDto carDto = new CarDto(car);

        if (dealer != null) {
            DealerDto dealerDto = new DealerDto(dealer);
            carDto.setDealer(dealerDto);
        }
        carDto.setDealer_id(car.getDealerId());
        return carDto;
    }


    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteBooking(@RequestParam int id) {
        try {
            pendingBookingService.deleteBooking(id);
            return ResponseEntity.status(HttpStatus.OK).body("Successful");
        } catch (BookingNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Unsuccessful");
        }
    }

    @PatchMapping("/bookingStatus")
    public ResponseEntity<?> statusUpdate(@RequestBody PendingBookingDTO pendingBookingDTO) {
        try {
            pendingBookingService.statusUpdate(pendingBookingDTO);
            return ResponseEntity.status(HttpStatus.OK).body("Successful");
        } catch (BookingNotFound | CarNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Unsuccessful");

        }

    }

    @GetMapping("/getAllpendingBookings")
    public ResponseEntity<ResponseAllPendingBookingDto> getAllpendingBookings(@RequestParam int pageNo) {
        try {
            List<PendingBookingDTO> listOfPendingBooking = pendingBookingService.getAllPendingBookingWithPage(pageNo);
            ResponseAllPendingBookingDto responseAllPendingBookingDto = new ResponseAllPendingBookingDto("success");
            responseAllPendingBookingDto.setList(listOfPendingBooking);
            return ResponseEntity.status(HttpStatus.OK).body(responseAllPendingBookingDto);

        } catch (CarNotFoundException carNotFoundException) {
            ResponseAllPendingBookingDto responseAllPendingBookingDto = new ResponseAllPendingBookingDto("unsuccess");
            responseAllPendingBookingDto.setException("Pending Booking not faund");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseAllPendingBookingDto);
        } catch (PageNotFoundException pageNotFoundException) {
            ResponseAllPendingBookingDto responseAllPendingBookingDto = new ResponseAllPendingBookingDto("unsuccess");
            responseAllPendingBookingDto.setException("page not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseAllPendingBookingDto);

        }


    }


    @GetMapping("getByUserId")
    public ResponseEntity<ResponseAllPendingBookingDto> getByUserId(@RequestParam int pageNo) {
        try {
            List<PendingBookingDTO> listOfPendingBooking = pendingBookingService.getAllPendingBookingWithPage(pageNo);
            ResponseAllPendingBookingDto responseAllPendingBookingDto = new ResponseAllPendingBookingDto("success");
            responseAllPendingBookingDto.setList(listOfPendingBooking);
            return ResponseEntity.status(HttpStatus.OK).body(responseAllPendingBookingDto);
        } catch (CarNotFoundException carNotFoundException) {
            ResponseAllPendingBookingDto responseAllPendingBookingDto = new ResponseAllPendingBookingDto("unsuccess");
            responseAllPendingBookingDto.setException("Pending Booking not faund");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseAllPendingBookingDto);
        } catch (PageNotFoundException pageNotFoundException) {
            ResponseAllPendingBookingDto responseAllPendingBookingDto = new ResponseAllPendingBookingDto("unsuccess");
            responseAllPendingBookingDto.setException("page not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseAllPendingBookingDto);
        }
    }
}




