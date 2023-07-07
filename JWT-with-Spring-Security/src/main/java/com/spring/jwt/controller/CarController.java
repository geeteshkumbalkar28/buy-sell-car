package com.spring.jwt.controller;


import com.spring.jwt.dto.*;
import com.spring.jwt.exception.CarNotFoundException;
import com.spring.jwt.exception.PageNotFoundException;
import com.spring.jwt.service.ICarRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/car")
public class CarController {
    @Autowired
    private ICarRegister iCarRegister;

    @PostMapping(value = "/carregister")
    public ResponseEntity<ResponseDto> carRegistration(@RequestBody CarDto carDto) {
        try{
            String result = iCarRegister.AddCarDetails(carDto);


            return (ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("success",result)));

        }catch (CarNotFoundException carNotFoundException){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("unsuccess","Dealer not found"));
        }
//        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<ResponseDto> carEdit(@RequestBody CarDto carDto, @PathVariable int id) {
        try {

            String result = iCarRegister.editCarDetails(carDto, id);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("success",result));

        }catch (CarNotFoundException carNotFoundException){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("unsuccess","car not found"));
        }

    }
    @GetMapping("/getAllCars")
    public ResponseEntity<ResponseAllCarDto> getAllCars(@RequestParam int pageNo){
        try
        {
            List<CarDto> listOfCar= iCarRegister.getAllCarsWithPages(pageNo);

            ResponseAllCarDto responseAllCarDto = new ResponseAllCarDto("success");
            responseAllCarDto.setList(listOfCar);
            return ResponseEntity.status(HttpStatus.OK).body(responseAllCarDto);

        }
    catch (CarNotFoundException carNotFoundException){
//            List<CarDto> emptyList;
        ResponseAllCarDto responseAllCarDto = new ResponseAllCarDto("unsuccess");
        responseAllCarDto.setException("car not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseAllCarDto);

    }
        catch (PageNotFoundException pageNotFoundException){
            ResponseAllCarDto responseAllCarDto = new ResponseAllCarDto("unsuccess");
            responseAllCarDto.setException("page not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseAllCarDto);

        }
    }
//    @GetMapping("/getAllCars")
//    public List<CarDto> getAllCars(@RequestParam int pageNo){
//        return iCarRegister.getAllCarsWithPages(pageNo);
//    }
    @DeleteMapping("/removeCar")
    public ResponseEntity<ResponseDto> deleteCar(@RequestParam int carId){
        try {

            String result =iCarRegister.deleteCar(carId);

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("success",result));
        }
        catch (CarNotFoundException carNotFoundException){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("unsuccess","car not found"));

        }
    }

    @GetMapping("/getCar")
    public ResponseEntity<ResponseSingleCarDto> findByArea(@RequestParam int car_id) {
        try {
            ResponseSingleCarDto responseSingleCarDto = new ResponseSingleCarDto("success");

            CarDto car = iCarRegister.findById(car_id);

            responseSingleCarDto.setObject(car);
            return ResponseEntity.status(HttpStatus.OK).body(responseSingleCarDto);
        }catch (CarNotFoundException carNotFoundException){
            ResponseSingleCarDto responseSingleCarDto = new ResponseSingleCarDto("unsuccess");
            responseSingleCarDto.setException("car not found by car id");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseSingleCarDto);
        }

//        return ResponseEntity.ok(cars.get());*
    }
    @GetMapping("/mainFilter/{pageNo}")
    public ResponseEntity<ResponseAllCarDto> searchByFilter(@RequestBody FilterDto filterDto, @PathVariable int pageNo){
        try{

            List<CarDto> listOfCar= iCarRegister.searchByFilter(filterDto,pageNo);
            ResponseAllCarDto responseAllCarDto = new ResponseAllCarDto("success");
            responseAllCarDto.setList(listOfCar);
            return ResponseEntity.status(HttpStatus.OK).body(responseAllCarDto);

        }
        catch (PageNotFoundException pageNotFoundException){
            ResponseAllCarDto responseAllCarDto = new ResponseAllCarDto("unsuccess");
            responseAllCarDto.setException("page not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseAllCarDto);
        }
    }
    @GetMapping("/dealer/{dealerId}/status/{carStatus}")
    public ResponseEntity<List<CarDto>> getCarsByDealerIdAndStatus(
            @PathVariable("dealerId") Integer dealerId,
            @PathVariable("carStatus") String carStatus
    ) {
        List<CarDto> cars = iCarRegister.getCarsByDealerIdWithStatus(dealerId, carStatus);
        return ResponseEntity.ok(cars);
    }


}



