package com.spring.jwt.service;


import com.spring.jwt.Interfaces.BookingService;
import com.spring.jwt.Interfaces.ICarRegister;
import com.spring.jwt.dto.BookingDto;
import com.spring.jwt.dto.BookingResponse;
import com.spring.jwt.dto.CarDto;
import com.spring.jwt.entity.*;
import com.spring.jwt.exception.*;
import com.spring.jwt.repository.BookingRepository;
import com.spring.jwt.repository.CarRepo;
import com.spring.jwt.repository.PendingBookingRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            throw new BookingException("Car is not available for booking as it's already sold.");
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

    @Override
    public List<BookingDto> getAllBooking(int pageNo) {
        List<Booking> listOfBooking = bookingRepository.findAll();
        CarNotFoundException carNotFoundException;
        if((pageNo*10)>listOfBooking.size()-1){
            throw new PageNotFoundException("page not found");

        }
        if(listOfBooking.size()<=0){throw new BookingNotFound("car not found",HttpStatus.NOT_FOUND);}
//        System.out.println("list of de"+listOfCar.size());
        List<BookingDto> listOfBookingDto = new ArrayList<>();

        int pageStart=pageNo*10;
        int pageEnd=pageStart+10;
        int diff=(listOfBooking.size()) - pageStart;
        for(int counter=pageStart,i=1;counter<pageEnd;counter++,i++){
            if(pageStart>listOfBooking.size()){break;}

//            System.out.println("*");
            BookingDto bookingDto = new BookingDto(listOfBooking.get(counter));
            bookingDto.setId(listOfBooking.get(counter).getId());
            bookingDto.setDate(listOfBooking.get(counter).getDate());
            bookingDto.setPrice(listOfBooking.get(counter).getPrice());
            bookingDto.setCarId(listOfBooking.get(counter).getCarId());
            bookingDto.setUserId(listOfBooking.get(counter).getUserId());
            bookingDto.setDealerId(listOfBooking.get(counter).getDealerId());
           // bookingDto.setPendingBookingId(listOfBooking.get(counter).);
            listOfBookingDto.add(bookingDto);
            if(diff == i){
                break;
            }
        }

       System.out.println(listOfBookingDto);
        return listOfBookingDto;
    }

    @Override
    public BookingDto getAllBookingsByUserId(int userId) {
        Optional<Booking> booking=bookingRepository.findByUserId(userId);
        if (booking.isPresent()){
            return new BookingDto(booking.get());
        }else {
            throw new BookingNotFoundException("Booking not found");
        }
    }

    @Override
    public List<BookingDto> getAllBookingsByDealerId(int dealerId,int pageNo) {
        List<Booking> listOfBooking = bookingRepository.findByDealerId(dealerId);
        CarNotFoundException carNotFoundException;
        if((pageNo*10)>listOfBooking.size()-1){
            throw new PageNotFoundException("page not found");

        }
        if(listOfBooking.size()<=0){throw new BookingNotFound("car not found",HttpStatus.NOT_FOUND);}
//        System.out.println("list of de"+listOfCar.size());
        List<BookingDto> listOfBookingDto = new ArrayList<>();

        int pageStart=pageNo*10;
        int pageEnd=pageStart+10;
        int diff=(listOfBooking.size()) - pageStart;
        for(int counter=pageStart,i=1;counter<pageEnd;counter++,i++){
            if(pageStart>listOfBooking.size()){break;}

//            System.out.println("*");
            BookingDto bookingDto = new BookingDto(listOfBooking.get(counter));
            bookingDto.setId(listOfBooking.get(counter).getId());
            bookingDto.setDate(listOfBooking.get(counter).getDate());
            bookingDto.setPrice(listOfBooking.get(counter).getPrice());
            bookingDto.setCarId(listOfBooking.get(counter).getCarId());
            bookingDto.setUserId(listOfBooking.get(counter).getUserId());
            bookingDto.setDealerId(listOfBooking.get(counter).getDealerId());
            // bookingDto.setPendingBookingId(listOfBooking.get(counter).);
            listOfBookingDto.add(bookingDto);
            if(diff == i){
                break;
            }
        }

        System.out.println(listOfBookingDto);
        return listOfBookingDto;
    }

    @Override
    public BookingDto getBookingById(int id) {
       Optional<Booking> booking= bookingRepository.findById(id);
       if (booking.isPresent()){
           return new BookingDto(booking.get());
       }else {
           throw new BookingNotFoundException("No booking found");
       }
    }

}
