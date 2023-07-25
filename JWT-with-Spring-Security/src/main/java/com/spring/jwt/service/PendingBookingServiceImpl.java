package com.spring.jwt.service;

import com.spring.jwt.dto.BookingDtos.PendingBookingRequestDto;
import com.spring.jwt.dto.BookingDtos.PendingBookingResponseDealerDto;
import com.spring.jwt.dto.BookingDtos.PendingBookingResponseForSingleDealerDto;
import com.spring.jwt.dto.CarDto;
import com.spring.jwt.dto.DealerDto;
import com.spring.jwt.dto.PendingBookingDTO;
import com.spring.jwt.entity.Car;
import com.spring.jwt.entity.Dealer;
import com.spring.jwt.entity.PendingBooking;
import com.spring.jwt.entity.User;
import com.spring.jwt.exception.*;
import com.spring.jwt.repository.CarRepo;
import com.spring.jwt.repository.DealerRepository;
import com.spring.jwt.repository.PendingBookingRepository;
import com.spring.jwt.Interfaces.PendingBookingService;
import com.spring.jwt.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DealerRepository dealerRepository;

    @Autowired
    private UserRepository userRepository;

    @Override

    public void deleteBooking(int id) {
        Optional<PendingBooking> pendingBooking = pendingBookingRepository.findById(id);
        if (pendingBooking.isPresent()) {
            pendingBookingRepository.deleteById(id);
        } else {
            throw new BookingNotFound("Booking not found");
        }
    }

    @Override
    public void statusUpdate(PendingBookingDTO pendingBookingDTO) {
        Optional<PendingBooking> pendingBookingOptional = pendingBookingRepository.findById(pendingBookingDTO.getId());
        if (pendingBookingOptional.isPresent()) {
            PendingBooking pendingBooking = pendingBookingOptional.get();
            pendingBooking.setStatus(pendingBookingDTO.getStatus());
            Optional<Car> carOptional = carRepository.findById(pendingBookingDTO.getCarId());
            if (carOptional.isPresent()) {
                Car car = carOptional.get();
                car.setCarStatus(pendingBookingDTO.getStatus());
                carRepository.save(car);
            } else {
                throw new CarNotFoundException("No car found with this id");
            }
            pendingBookingRepository.save(pendingBooking);
        } else {
            throw new BookingNotFound("Booking not found");
        }
    }


    public List<PendingBookingDTO> getAllPendingBookingWithPage(int PageNo) {

        List<PendingBooking> listofPendingBooking = pendingBookingRepository.findAll();
        if ((PageNo * 10) > listofPendingBooking.size() - 1) {
            throw new PageNotFoundException("page not found");

        }
        if (listofPendingBooking.size() <= 0) {
            throw new CarNotFoundException("Pending Booking not found", HttpStatus.NOT_FOUND);
        }

        List<PendingBookingDTO> listOfPendingBookingdto = new ArrayList<>();

        int pageStart = PageNo * 10;
        int pageEnd = pageStart + 10;
        int diff = (listofPendingBooking.size()) - pageStart;
        for (int counter = pageStart, i = 1; counter < pageEnd; counter++, i++) {
            if (pageStart > listofPendingBooking.size()) {
                break;
            }


            PendingBookingDTO pendingBookingDTO = new PendingBookingDTO(listofPendingBooking.get(counter));
            pendingBookingDTO.setCarId(listofPendingBooking.get(counter).getId());
            listOfPendingBookingdto.add(pendingBookingDTO);
            if (diff == i) {
                break;
            }
        }

        return listOfPendingBookingdto;
    }


    @Override
    public PendingBookingRequestDto savePendingBooking(PendingBookingDTO pendingBookingDTO) {
        Optional<Car> car = carRepository.findById(pendingBookingDTO.getCarId());
        if (car.isEmpty()){throw new CarNotFoundException("car not found by id");}

        Optional<Dealer> dealer = dealerRepository.findById(car.get().getDealerId());
        if(dealer.isEmpty()){throw new DealerNotFoundException("dealer not found by id");}

        Optional<User> user=userRepository.findById(pendingBookingDTO.getUserId());
        if(user.isEmpty()){throw new UserNotFoundExceptions("user not found by id");}

        PendingBooking pendingBooking = new PendingBooking(pendingBookingDTO);
        pendingBooking.setCarCar(car.get());
        pendingBooking.setDealerId(pendingBookingDTO.getDealerId());
        pendingBookingRepository.save(pendingBooking);

        PendingBookingRequestDto pendingBookingRequestDto = new PendingBookingRequestDto(car.get());
        PendingBookingResponseDealerDto pendingBookingResponseDealerDto = new PendingBookingResponseDealerDto(dealer.get());
        pendingBookingRequestDto.setPendingBookingResponseDealerDto(pendingBookingResponseDealerDto);
        return pendingBookingRequestDto;





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

    @Override
    public com.spring.jwt.dto.BookingDtos.PendingBookingDTO getPendingBookingId(int bookingId) {
        Optional<PendingBooking> pendingBooking = pendingBookingRepository.findById(bookingId);
        if (pendingBooking.isEmpty()) {
            throw new BookingNotFoundException("pending booking not found");
        }
        com.spring.jwt.dto.BookingDtos.PendingBookingDTO pendingBookingDTO = new com.spring.jwt.dto.BookingDtos.PendingBookingDTO(pendingBooking.get());

        return pendingBookingDTO;

    }

    @Override
    public List<com.spring.jwt.dto.BookingDtos.PendingBookingDTO> getPendingBookingsByDealerId(int pageNo, int dealerId) {
        Optional<Dealer> dealer = dealerRepository.findById(dealerId);
        if (dealer.isEmpty()) {
            throw new DealerNotFoundException("dealer not found by id ");
        }
        Optional<List<PendingBooking>> listofPendingBooking = pendingBookingRepository.findByDealerId(dealerId);
        if ((pageNo * 10) > listofPendingBooking.get().size() - 1) {
            throw new PageNotFoundException("page not found");

        }
        if (listofPendingBooking.isEmpty()) {
            throw new BookingNotFoundException("pending booking not found by dealer Id");
        }
        List<com.spring.jwt.dto.BookingDtos.PendingBookingDTO> listOfPendingBookingdto = new ArrayList<>();
        int pageStart = pageNo * 10;
        int pageEnd = pageStart + 10;
        int diff = (listofPendingBooking.get().size()) - pageStart;
        for (int counter = pageStart, i = 1; counter < pageEnd; counter++, i++) {
            if (pageStart > listofPendingBooking.get().size()) {
                break;
            }


            com.spring.jwt.dto.BookingDtos.PendingBookingDTO pendingBookingDTO = new com.spring.jwt.dto.BookingDtos.PendingBookingDTO(listofPendingBooking.get().get(counter));
            listOfPendingBookingdto.add(pendingBookingDTO);
            if (diff == i) {
                break;
            }
        }
        return listOfPendingBookingdto;
    }

    @Override
    public List<com.spring.jwt.dto.BookingDtos.PendingBookingDTO> getPendingBookingsByCarId(int pageNo, int carId) {
        Optional<Car> car = carRepository.findById(carId);
        if (car.isEmpty()) {
            throw new CarNotFoundException("car not found by id");
        }
        Optional<List<PendingBooking>> listofPendingBooking = pendingBookingRepository.findByDealerId(carId);
        if ((pageNo * 10) > listofPendingBooking.get().size() - 1) {
            throw new PageNotFoundException("page not found");
        }
        if (listofPendingBooking.isEmpty()) {
            throw new BookingNotFoundException("pending booking not found by car Id");
        }
        List<com.spring.jwt.dto.BookingDtos.PendingBookingDTO> listOfPendingBookingdto = new ArrayList<>();
        int pageStart = pageNo * 10;
        int pageEnd = pageStart + 10;
        int diff = (listofPendingBooking.get().size()) - pageStart;
        for (int counter = pageStart, i = 1; counter < pageEnd; counter++, i++) {
            if (pageStart > listofPendingBooking.get().size()) {
                break;
            }
            com.spring.jwt.dto.BookingDtos.PendingBookingDTO pendingBookingDTO = new com.spring.jwt.dto.BookingDtos.PendingBookingDTO(listofPendingBooking.get().get(counter));
            listOfPendingBookingdto.add(pendingBookingDTO);
            if (diff == i) {
                break;
            }
        }
        return listOfPendingBookingdto;
    }

    @Override
    public List<com.spring.jwt.dto.BookingDtos.PendingBookingDTO> getAllPendingBookingByUserId(int pageNo, int userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotDealerException("User  not found by id");
        }
        Optional<List<PendingBooking>> listofPendingBooking = pendingBookingRepository.getAllPendingBookingByUserId(userId);
        if ((pageNo * 10) > listofPendingBooking.get().size() - 1) {
            throw new PageNotFoundException("page not found");
        }
        if (listofPendingBooking.isEmpty()) {
            throw new BookingNotFoundException("pending booking not found by User id");
        }
        List<com.spring.jwt.dto.BookingDtos.PendingBookingDTO> listOfPendingBookingdto = new ArrayList<>();
        int pageStart = pageNo * 10;
        int pageEnd = pageStart + 10;
        int diff = (listofPendingBooking.get().size()) - pageStart;
        for (int counter = pageStart, i = 1; counter < pageEnd; counter++, i++) {
            if (pageStart > listofPendingBooking.get().size()) {
                break;
            }
            com.spring.jwt.dto.BookingDtos.PendingBookingDTO pendingBookingDTO = new com.spring.jwt.dto.BookingDtos.PendingBookingDTO(listofPendingBooking.get().get(counter));
            listOfPendingBookingdto.add(pendingBookingDTO);
            if (diff == i) {
                break;
            }
        }

        return listOfPendingBookingdto;


    }
}
