package com.spring.jwt.controller;

import com.spring.jwt.dto.PasswordChange;
import com.spring.jwt.dto.ResponseAllUsersDto;
import com.spring.jwt.dto.UserProfileDto;
import com.spring.jwt.entity.Userprofile;
import com.spring.jwt.exception.InvalidPasswordException;
import com.spring.jwt.exception.PageNotFoundException;
import com.spring.jwt.exception.UserNotFoundExceptions;
import com.spring.jwt.service.UserService;
import com.spring.jwt.utils.BaseResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/index")
    public ResponseEntity<String> index(Principal principal){
        return ResponseEntity.ok("Welcome to user page : " + principal.getName());
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<ResponseAllUsersDto> getAllUsers(@RequestParam int pageNo) {
        try {
            // Retrieve the list of users for the specified page number
            List<UserProfileDto> list = userService.getAllUsers(pageNo);

            // Create a ResponseAllUsersDto with success status and set the list of users
            ResponseAllUsersDto responseAllUsersDto = new ResponseAllUsersDto("successful");
            responseAllUsersDto.setList(list);

            // Return ResponseEntity with success status and the ResponseAllUsersDto
            return ResponseEntity.status(HttpStatus.OK).body(responseAllUsersDto);

        } catch (UserNotFoundExceptions exception) {

            // Handle UserNotFoundException and create a ResponseAllUsersDto with error status and message
            ResponseAllUsersDto responseAllCarDto = new ResponseAllUsersDto("unsuccessful");
            responseAllCarDto.setException("car not found");

            // Return ResponseEntity with error status and the ResponseAllUsersDto
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseAllCarDto);
        } catch (PageNotFoundException exception) {

            // Handle PageNotFoundException and create a ResponseAllUsersDto with error status and message
            ResponseAllUsersDto responseAllCarDto = new ResponseAllUsersDto("unsuccessful");
            responseAllCarDto.setException("page not found");

            // Return ResponseEntity with error status and the ResponseAllUsersDto
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseAllCarDto);
        }
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<?> editUser(@RequestBody UserProfileDto userProfileDto, @PathVariable int id) {
        try {

            // Call the editUser method in the userService to update the user profile based on the provided DTO and ID.
            BaseResponseDTO result = userService.editUser(userProfileDto, id);

            // Return a ResponseEntity with HTTP status OK and a body containing a success message and the result message.
            return ResponseEntity.status(HttpStatus.OK).body(new BaseResponseDTO("Successful", result.getMessage()));
        } catch (UserNotFoundExceptions exception) {

            // If the user with the given ID is not found, catch the UserNotFoundExceptions and return a ResponseEntity
            // with HTTP status NOT_FOUND and a body containing an error message.
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseResponseDTO("Unsuccessful", "user not found"));
        }
    }

    public ResponseEntity<?> removeUser(@PathVariable int id) {
        try {

            // Attempt to remove the user using the UserService's removeUser method.
            BaseResponseDTO result = userService.removeUser(id);

            // Return a success response entity with HTTP status code 200 and a success message.
            return ResponseEntity.status(HttpStatus.OK).body(new BaseResponseDTO("Successful", result.getMessage()));
        } catch (UserNotFoundExceptions exception) {

            // If the user is not found, return a not found response entity with HTTP status code 404 and an error message.
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseResponseDTO("Unsuccessful", "user not found"));
        }
    }

    @GetMapping("/getUser/{id}")
    public ResponseEntity<?> getUser(@PathVariable int id) {

        try {

            // Call the userService to retrieve the user profile data for the given ID
            UserProfileDto userProfileDto = userService.getUser(id);

            // Return a ResponseEntity with the user profile data and HTTP status code 200 (OK)
            return ResponseEntity.status(HttpStatus.OK).body(userProfileDto);
        } catch (UserNotFoundExceptions exception) {

            // The user was not found, so return a ResponseEntity with an error message and HTTP status code 404 (NOT_FOUND)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseResponseDTO("Unsuccessful", "user not found"));
        }
    }

    @PutMapping("/changePassword/{id}")
    public ResponseEntity<BaseResponseDTO> changePassword(@PathVariable int id, @RequestBody PasswordChange passwordChange) {
        try {

            // Attempt to change the user's password using the UserService.
            BaseResponseDTO result = userService.changePassword(id, passwordChange);

            // Return a successful response with the updated user details.
            return ResponseEntity.status(HttpStatus.OK).body(new BaseResponseDTO("Successful", result.getMessage()));
        } catch (UserNotFoundExceptions exception) {

            // Return a response indicating that the user was not found.
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseResponseDTO("Unsuccessfully", "UserNotFoundException"));
        } catch (InvalidPasswordException exception) {

            // Return a response indicating an invalid password was provided.
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseResponseDTO("Unsuccessfully", "InvalidPasswordException"));
        }
    }
}
