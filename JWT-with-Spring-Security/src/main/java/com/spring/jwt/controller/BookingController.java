package com.spring.jwt.controller;


import com.spring.jwt.Interfaces.BookingService;
import com.spring.jwt.dto.BookingDto;
import com.spring.jwt.dto.ResponseBookingDto;
import com.spring.jwt.exception.BookingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/confirmBooking")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/book")
    public ResponseEntity<ResponseBookingDto> saveBooking(@RequestBody BookingDto bookingDTO) {
        try {
            BookingDto savedBooking = bookingService.saveBooking(bookingDTO);
            ResponseBookingDto responseBookingDto = new ResponseBookingDto("Success");
            return ResponseEntity.status(HttpStatus.CREATED).body(responseBookingDto);
        } catch (BookingException e) {
            ResponseBookingDto responseBookingDto = new ResponseBookingDto("unsuccess");
            responseBookingDto.setException(String.valueOf(e));
            return ResponseEntity.status(HttpStatus.CREATED).body(responseBookingDto);
        }
    }
}
