package com.spring.jwt.controller;

import com.spring.jwt.dto.*;
import com.spring.jwt.exception.CarNotFoundException;
import com.spring.jwt.exception.PageNotFoundException;
import com.spring.jwt.exception.UserNotFoundExceptions;
import com.spring.jwt.service.FilterService;
import com.spring.jwt.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cars")
public class FilterController {
    @Autowired
    private final FilterService filterService;

    @Autowired
    private UserService userService;


    @GetMapping("/mainFilter/{pageNo}")
    public ResponseEntity<ResponseAllCarDto> searchByFilter(
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) String area,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) String transmission,
            @RequestParam(required = false) String fuelType,
            @PathVariable int pageNo) {


        Integer convertedYear = year != null && !year.isEmpty() ? Integer.valueOf(year) : null;


        FilterDto filterDto = new FilterDto(minPrice, maxPrice, area, brand, model, transmission, fuelType, convertedYear);


        try {
            List<CarDto> listOfCar = filterService.searchByFilter(filterDto, pageNo);
            ResponseAllCarDto responseAllCarDto = new ResponseAllCarDto("success");
            responseAllCarDto.setList(listOfCar);
            return ResponseEntity.status(HttpStatus.OK).body(responseAllCarDto);
        } catch (PageNotFoundException pageNotFoundException) {
            ResponseAllCarDto responseAllCarDto = new ResponseAllCarDto("unsuccess");
            responseAllCarDto.setException("page not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseAllCarDto);
        }
    }

    @GetMapping("/getAllCars")
    public ResponseEntity<ResponseAllCarDto> getAllCars(@RequestParam int pageNo){
        try
        {
            List<CarDto> listOfCar= filterService.getAllCarsWithPages(pageNo);

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
    @PostMapping("/forgot-password")
    public ResponseEntity<ResponseDto> forgotPass(HttpServletRequest request) throws UserNotFoundExceptions {

        try {
            String email = request.getParameter("email");
            String token = RandomStringUtils.randomAlphabetic(40);

            // Calculate the expiration time
            LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(1); // Set the expiration time to 24 hours from now

            userService.updateResetPassword(token, email);
            String resetPasswordLink = "http://" + request.getServerName() + "/reset-password?token=" + token;
            ResponseDto response = userService.forgotPass(email, resetPasswordLink, request.getServerName());

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("Successful",response.getMessage()));
        }catch (UserNotFoundExceptions e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("Unsuccessful","Invalid email please register"));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ResponseDto> resetPassword(@RequestBody ResetPassword resetPassword) throws UserNotFoundExceptions {

        try {
            String token = resetPassword.getToken();
            String newPassword = resetPassword.getPassword();
            ResponseDto response = userService.updatePassword(token, newPassword);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("Successful",response.getMessage()));
        }catch (UserNotFoundExceptions e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("Unsuccessful","Something went wrong"));
        }
    }

}
