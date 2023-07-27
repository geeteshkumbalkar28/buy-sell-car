package com.spring.jwt.controller;


import com.spring.jwt.Interfaces.BookingService;
import com.spring.jwt.dto.BookingDto;
import com.spring.jwt.dto.BookingResponse;
import com.spring.jwt.dto.ResponseAllBookingDto;
import com.spring.jwt.dto.ResponseBookingDto;
import com.spring.jwt.entity.Booking;
import com.spring.jwt.exception.BookingException;
import com.spring.jwt.exception.BookingNotFound;
import com.spring.jwt.exception.BookingNotFoundException;
import com.spring.jwt.exception.PageNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.desktop.UserSessionEvent;
import java.util.List;

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

    @GetMapping("/getAllBookings")
    public ResponseEntity<ResponseAllBookingDto> getAllBookings(@RequestParam int pageNo) {
        try {
            List<BookingDto> bookings = bookingService.getAllBooking(pageNo);
            ResponseAllBookingDto responseAllBookingDto = new ResponseAllBookingDto("Successful");
            responseAllBookingDto.setMessage("All the bookings");
            responseAllBookingDto.setBookings(bookings);
            return ResponseEntity.status(HttpStatus.OK).body(responseAllBookingDto);
        } catch (PageNotFoundException e) {
            ResponseAllBookingDto responseAllBookingDto = new ResponseAllBookingDto("Unsuccessful");
            responseAllBookingDto.setMessage("Page Not found");
            responseAllBookingDto.setException(String.valueOf(e));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseAllBookingDto);
        } catch (BookingNotFoundException | BookingException e) {
            ResponseAllBookingDto responseAllBookingDto = new ResponseAllBookingDto("Unsuccessful");
            responseAllBookingDto.setMessage("Booking not found");
            responseAllBookingDto.setException(String.valueOf(e));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseAllBookingDto);
        }
    }

    @GetMapping("/getAllBookingsByUserId")
    public ResponseEntity<BookingResponse> getAllBookingsByUserId(@RequestParam int userId){
        try {
            BookingDto bookingDto= bookingService.getAllBookingsByUserId(userId);
            BookingResponse bookingResponse= new BookingResponse("Successful");
            bookingResponse.setBookingDto(bookingDto);
            return ResponseEntity.status(HttpStatus.OK).body(bookingResponse);
        }catch (BookingNotFoundException e){
            BookingResponse bookingResponse= new BookingResponse("Unsuccessful");
            bookingResponse.setException(String.valueOf(e));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(bookingResponse);
        }
    }

    @GetMapping("/getAllBookingsByDealerId")
    public ResponseEntity<ResponseAllBookingDto> getAllBookingsByDealerId(@RequestParam int dealerId, @RequestParam int pageNo){
        try {
            List<BookingDto> bookingDto=bookingService.getAllBookingsByDealerId(dealerId,pageNo);
            ResponseAllBookingDto responseAllBookingDto= new ResponseAllBookingDto("Successful");
            responseAllBookingDto.setMessage("All bookings from dealer");
            responseAllBookingDto.setBookings(bookingDto);
            return ResponseEntity.status(HttpStatus.OK).body(responseAllBookingDto);
        }catch (BookingNotFoundException e){
            ResponseAllBookingDto responseAllBookingDto= new ResponseAllBookingDto("Unsuccessful");
            responseAllBookingDto.setMessage("Booking not found");
            responseAllBookingDto.setException(String.valueOf(e));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseAllBookingDto);
        }catch (PageNotFoundException e) {
            ResponseAllBookingDto responseAllBookingDto = new ResponseAllBookingDto("Unsuccessful");
            responseAllBookingDto.setMessage("Page Not found");
            responseAllBookingDto.setException(String.valueOf(e));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseAllBookingDto);
        }
    }

    @GetMapping("/getBookingById")
    public ResponseEntity<BookingResponse> getBookingById(int id){
        try {
            BookingDto bookingDto=bookingService.getBookingById(id);
            BookingResponse bookingResponse= new BookingResponse("Successful");
            bookingResponse.setBookingDto(bookingDto);
            return ResponseEntity.status(HttpStatus.OK).body(bookingResponse);
        }catch (BookingNotFoundException e){
            BookingResponse bookingResponse= new BookingResponse("Unsuccessful");
            bookingResponse.setException(String.valueOf(e));
            return ResponseEntity.status(HttpStatus.OK).body(bookingResponse);
        }
    }
}
