package com.spring.jwt.controller;


import com.spring.jwt.dto.*;
import com.spring.jwt.dto.BookingDtos.*;
import com.spring.jwt.dto.PendingBookingDTO;
import com.spring.jwt.entity.*;

import com.spring.jwt.exception.*;


import com.spring.jwt.repository.CarRepo;
import com.spring.jwt.repository.DealerRepository;
import com.spring.jwt.Interfaces.PendingBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private CarRepo carRepo;
    @Autowired
    private DealerRepository dealerRepository;
    @Autowired
    private PendingBookingService pendingBookingService;

    @PostMapping("/request")
    public ResponseEntity<?> requestCarBooking(@RequestBody PendingBookingDTO pendingBookingDTO) {
        try{

            PendingBookingRequestDto pendingBooking = pendingBookingService.savePendingBooking(pendingBookingDTO);
            ResponsePendingBookingRequestDto responsePendingBookingRequestDto = new ResponsePendingBookingRequestDto("success");
            responsePendingBookingRequestDto.setPendingBookingRequestDto(pendingBooking);
            return ResponseEntity.status(HttpStatus.OK).body(pendingBooking);

        }catch (CarNotFoundException carNotFoundException){
            ResponsePendingBookingRequestDto responsePendingBookingRequestDto = new ResponsePendingBookingRequestDto("success");
            responsePendingBookingRequestDto.setException(String.valueOf(carNotFoundException));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responsePendingBookingRequestDto);

        }catch (UserNotFoundExceptions userNotFoundExceptions){
            ResponsePendingBookingRequestDto responsePendingBookingRequestDto = new ResponsePendingBookingRequestDto("success");
            responsePendingBookingRequestDto.setException(String.valueOf(userNotFoundExceptions));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responsePendingBookingRequestDto);

        }catch (DealerNotFoundException dealerNotFoundException){
            ResponsePendingBookingRequestDto responsePendingBookingRequestDto = new ResponsePendingBookingRequestDto("success");
            responsePendingBookingRequestDto.setException(String.valueOf(dealerNotFoundException));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responsePendingBookingRequestDto);

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
    public ResponseEntity<?> getByUserId(@RequestParam int pageNo,@RequestParam int userId) {
        try {
            List<com.spring.jwt.dto.BookingDtos.PendingBookingDTO> listOfPendingBooking = pendingBookingService.getAllPendingBookingByUserId(pageNo,userId);

            AllPendingBookingResponseDTO allPendingBookingResponseDTO = new AllPendingBookingResponseDTO("success");
            allPendingBookingResponseDTO.setList(listOfPendingBooking);

            return ResponseEntity.status(HttpStatus.OK).body(allPendingBookingResponseDTO);
        } catch (BookingNotFoundException bookingNotFoundException) {
            ResponseAllPendingBookingDto responseAllPendingBookingDto = new ResponseAllPendingBookingDto("unsuccess");
            responseAllPendingBookingDto.setException(String.valueOf(bookingNotFoundException));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseAllPendingBookingDto);
        } catch (PageNotFoundException pageNotFoundException) {
            ResponseAllPendingBookingDto responseAllPendingBookingDto = new ResponseAllPendingBookingDto("unsuccess");
            responseAllPendingBookingDto.setException(String.valueOf(pageNotFoundException));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseAllPendingBookingDto);
        }catch (UserNotFoundExceptions userNotFoundExceptions){
            ResponseAllPendingBookingDto responseAllPendingBookingDto = new ResponseAllPendingBookingDto("unsuccess");
            responseAllPendingBookingDto.setException(String.valueOf(userNotFoundExceptions));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseAllPendingBookingDto);

        }
    }

    @GetMapping("/getpendingBookingDeatailsById")
    public ResponseEntity<?> getBookingDetailsById(@RequestParam int bookingId) {
        try {
            com.spring.jwt.dto.BookingDtos.PendingBookingDTO pendingBookingDTO = pendingBookingService.getPendingBookingId(bookingId);
            PendingBookingResponseForSingleDealerDto pendingBookingResponseForSingleDealerDto = new PendingBookingResponseForSingleDealerDto("success");
            pendingBookingResponseForSingleDealerDto.setPendingBookingDTO(pendingBookingDTO);
            return ResponseEntity.status(HttpStatus.OK).body(pendingBookingResponseForSingleDealerDto);
        } catch (BookingNotFoundException bookingNotFoundException) {
            PendingBookingResponseForSingleDealerDto pendingBookingResponseForSingleDealerDto = new PendingBookingResponseForSingleDealerDto("unsuccess");
            pendingBookingResponseForSingleDealerDto.setException(String.valueOf(bookingNotFoundException));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(pendingBookingResponseForSingleDealerDto);

        }
    }
    @GetMapping("/getPendingBookingDeatilsByDealerID")
    public ResponseEntity<?> getBookingDetailsByDealerId(@RequestParam int pageNo,@RequestParam int dealerId) {
        try {
            List<com.spring.jwt.dto.BookingDtos.PendingBookingDTO> listOfPendingBooking = pendingBookingService.getPendingBookingsByDealerId(pageNo,dealerId);

            AllPendingBookingResponseDTO allPendingBookingResponseDTO = new AllPendingBookingResponseDTO("success");
            allPendingBookingResponseDTO.setList(listOfPendingBooking);

            return ResponseEntity.status(HttpStatus.OK).body(allPendingBookingResponseDTO);
        } catch (BookingNotFoundException bookingNotFoundException) {
            ResponseAllPendingBookingDto responseAllPendingBookingDto = new ResponseAllPendingBookingDto("unsuccess");
            responseAllPendingBookingDto.setException(String.valueOf(bookingNotFoundException));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseAllPendingBookingDto);
        } catch (PageNotFoundException pageNotFoundException) {
            ResponseAllPendingBookingDto responseAllPendingBookingDto = new ResponseAllPendingBookingDto("unsuccess");
            responseAllPendingBookingDto.setException(String.valueOf(pageNotFoundException));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseAllPendingBookingDto);
        }catch (DealerNotFoundException dealerNotFoundException){
            ResponseAllPendingBookingDto responseAllPendingBookingDto = new ResponseAllPendingBookingDto("unsuccess");
            responseAllPendingBookingDto.setException(String.valueOf(dealerNotFoundException));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseAllPendingBookingDto);

        }
    }
    @GetMapping("/getPendingBookingDeatilsByCarID")
    public ResponseEntity<?> getBookingDetailsByCarId(@RequestParam int pageNo,@RequestParam int CarId) {
        try {

            List<com.spring.jwt.dto.BookingDtos.PendingBookingDTO> listOfPendingBooking = pendingBookingService.getPendingBookingsByCarId(pageNo,CarId);

            AllPendingBookingResponseDTO allPendingBookingResponseDTO = new AllPendingBookingResponseDTO("success");
            allPendingBookingResponseDTO.setList(listOfPendingBooking);


            return ResponseEntity.status(HttpStatus.OK).body(allPendingBookingResponseDTO);
        } catch (BookingNotFoundException bookingNotFoundException) {
            ResponseAllPendingBookingDto responseAllPendingBookingDto = new ResponseAllPendingBookingDto("unsuccess");
            responseAllPendingBookingDto.setException(String.valueOf(bookingNotFoundException));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseAllPendingBookingDto);
        } catch (PageNotFoundException pageNotFoundException) {
            ResponseAllPendingBookingDto responseAllPendingBookingDto = new ResponseAllPendingBookingDto("unsuccess");
            responseAllPendingBookingDto.setException(String.valueOf(pageNotFoundException));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseAllPendingBookingDto);
        }catch (CarNotFoundException carNotFoundException){
            ResponseAllPendingBookingDto responseAllPendingBookingDto = new ResponseAllPendingBookingDto("unsuccess");
            System.err.println(carNotFoundException);
            responseAllPendingBookingDto.setException(carNotFoundException+" : car not found ");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseAllPendingBookingDto);

        }
    }
}




