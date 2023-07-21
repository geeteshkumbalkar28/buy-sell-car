package com.spring.jwt.controller;

import com.spring.jwt.Interfaces.PendingBookingService;
import com.spring.jwt.dto.*;
import com.spring.jwt.exception.BookingNotFound;
import com.spring.jwt.exception.CarNotFoundException;
import com.spring.jwt.exception.PageNotFoundException;
import com.spring.jwt.exception.UserNotFoundExceptions;
import com.spring.jwt.Interfaces.FilterService;
import com.spring.jwt.Interfaces.ICarRegister;
import com.spring.jwt.Interfaces.UserService;
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
    @Autowired
    private ICarRegister iCarRegister;

    private PendingBookingService pendingBookingService;


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

    /**
     * Retrieves a single car by its ID.
     *
     * @param carId The ID of the car to retrieve.
     * @return ResponseEntity containing the response for the request.
     *         If the car is found, the response will have a status of OK (200)
     *         and the car details will be included in the body.
     *         If the car is not found, the response will have a status of NOT_FOUND (404)
     *         and an error message will be included in the body.
     */
    @GetMapping("/getCar")
    public ResponseEntity<ResponseSingleCarDto> findByArea(@RequestParam int carId) {
        try {
            ResponseSingleCarDto responseSingleCarDto = new ResponseSingleCarDto("success");

            CarDto car = iCarRegister.findById(carId);

            responseSingleCarDto.setObject(car);
            return ResponseEntity.status(HttpStatus.OK).body(responseSingleCarDto);
        }catch (CarNotFoundException carNotFoundException){
            ResponseSingleCarDto responseSingleCarDto = new ResponseSingleCarDto("unsuccess");
            responseSingleCarDto.setException("car not found by car id");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseSingleCarDto);
        }

//        return ResponseEntity.ok(cars.get());*
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
            // Retrieve the email from the request
            String email = request.getParameter("email");

            // Generate a random token for password reset
            String token = RandomStringUtils.randomAlphabetic(40);

            // Calculate the expiration time (24 hours from now)
            LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(1);

            // Update the user's reset password token in the userService
            userService.updateResetPassword(token, email);

            // Construct the reset password link using the request's server name and token
            String resetPasswordLink = "http://" + request.getServerName() + "/reset-password?token=" + token;

            // Call the userService's forgotPass() method to send the password reset email
            ResponseDto response = userService.forgotPass(email, resetPasswordLink, request.getServerName());

            // Return a ResponseEntity object with the appropriate status and message
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("Successful", response.getMessage()));
        } catch (UserNotFoundExceptions e) {

            // Handle the case where the email does not belong to a registered user
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("Unsuccessful", "Invalid email. Please register."));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ResponseDto> resetPassword(@RequestBody ResetPassword resetPassword) throws UserNotFoundExceptions {

        try {
            String token = resetPassword.getToken();
            String newPassword = resetPassword.getPassword();

            // Invoke the userService.updatePassword() method to update the user's password
            ResponseDto response = userService.updatePassword(token, newPassword);

            // Return a ResponseEntity with success status (200) and a ResponseDto with success message
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("Successful", response.getMessage()));

        } catch (UserNotFoundExceptions e) {
            // If a UserNotFoundException is caught, return a ResponseEntity with not found status (404)
            // and a ResponseDto with an error message
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("Unsuccessful", "Something went wrong"));
        }
    }

}
