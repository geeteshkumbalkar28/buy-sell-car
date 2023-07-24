package com.spring.jwt.service;

import com.spring.jwt.dto.CarDto;
import com.spring.jwt.dto.DealerDto;
import com.spring.jwt.dto.PendingBookingDTO;
import com.spring.jwt.entity.Car;
import com.spring.jwt.entity.Dealer;
import com.spring.jwt.entity.PendingBooking;
import com.spring.jwt.exception.BookingNotFound;
import com.spring.jwt.exception.CarNotFoundException;
import com.spring.jwt.exception.PageNotFoundException;
import com.spring.jwt.repository.CarRepo;
import com.spring.jwt.repository.PendingBookingRepository;
import com.spring.jwt.Interfaces.PendingBookingService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
    public class PendingBookingServiceImpl implements PendingBookingService {

        private final PendingBookingRepository pendingBookingRepository;
        private final CarRepo carRepository;



    @Override

    public void deleteBooking(int id) {
       Optional<PendingBooking> pendingBooking= pendingBookingRepository.findById(id);
       if (pendingBooking.isPresent()){
           pendingBookingRepository.deleteById(id);
       }else {
           throw new BookingNotFound("Booking not found");
       }
    }

    @Override
    public void statusUpdate(PendingBookingDTO pendingBookingDTO) {
        Optional<PendingBooking> pendingBookingOptional= pendingBookingRepository.findById(pendingBookingDTO.getId());
        if (pendingBookingOptional.isPresent()) {
            PendingBooking pendingBooking = pendingBookingOptional.get();
            pendingBooking.setStatus(pendingBookingDTO.getStatus());
            Optional<Car> carOptional = carRepository.findById(pendingBookingDTO.getCarId());
            if (carOptional.isPresent()) {
                Car car = carOptional.get();
                car.setCarStatus(pendingBookingDTO.getStatus());
                carRepository.save(car);
            }else {
                throw new CarNotFoundException("No car found with this id");
            }
            pendingBookingRepository.save(pendingBooking);
        }else {
            throw new BookingNotFound("Booking not found");
        }
    }


    public List<PendingBookingDTO> getAllPendingBookingWithPage(int PageNo) {

        List<PendingBooking> listofPendingBooking = pendingBookingRepository.findAll();
        if((PageNo*10)>listofPendingBooking.size()-1){
            throw new PageNotFoundException("page not found");

        }
        if(listofPendingBooking.size()<=0){throw new CarNotFoundException("Pending Booking not found", HttpStatus.NOT_FOUND);}

        List<PendingBookingDTO> listOfPendingBookingdto = new ArrayList<>();

        int pageStart=PageNo*10;
        int pageEnd=pageStart+10;
        int diff=(listofPendingBooking.size()) - pageStart;
        for(int counter=pageStart,i=1;counter<pageEnd;counter++,i++){
            if(pageStart>listofPendingBooking.size()){break;}


            PendingBookingDTO pendingBookingDTO = new PendingBookingDTO (listofPendingBooking.get(counter));
            pendingBookingDTO.setCarId(listofPendingBooking.get(counter).getId());
            listOfPendingBookingdto.add(pendingBookingDTO);
            if(diff == i){
                break;
            }
        }

        return listOfPendingBookingdto;
    }

    @Override
    public List<PendingBookingDTO> getAllPendingBookingByUserIdWithPage(int PageNo) {
        return null;
    }

    @Override
    public PendingBooking savePendingBooking(PendingBookingDTO pendingBookingDTO) {
        PendingBooking pendingBooking = mapToPendingBooking(pendingBookingDTO);
        return pendingBookingRepository.save(pendingBooking);
    }

    private PendingBooking mapToPendingBooking(PendingBookingDTO pendingBookingDTO) {
        Optional<Car> optionalCar = carRepository.findById(pendingBookingDTO.getCarId());
        Car car = optionalCar.orElseThrow(() -> new EntityNotFoundException("Car not found"));


        int dealerId = Objects.requireNonNullElse(pendingBookingDTO.getDealerId(), -1);

        PendingBooking pendingBooking = new PendingBooking();
        pendingBooking.setDate(pendingBookingDTO.getDate());
        pendingBooking.setPrice(pendingBookingDTO.getPrice());
        pendingBooking.setStatus(pendingBookingDTO.getStatus());
        pendingBooking.setUserId(pendingBookingDTO.getUserId());
        pendingBooking.setDealerId(dealerId);
        pendingBooking.setAskingPrice(pendingBookingDTO.getAskingPrice());
        pendingBooking.setCarCar(car);
        return pendingBooking;
    }



    private Car mapToCar(CarDto carDto) {
        return new Car(carDto);
    }

    private Dealer mapToDealer(DealerDto dealerDto) {
        return new Dealer(dealerDto);
    }


}
