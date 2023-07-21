package com.spring.jwt.controller;

import com.spring.jwt.dto.PasswordChange;
import com.spring.jwt.dto.ResponseAllUsersDto;
import com.spring.jwt.dto.UserProfileDto;
import com.spring.jwt.exception.InvalidPasswordException;
import com.spring.jwt.exception.PageNotFoundException;
import com.spring.jwt.exception.UserNotFoundExceptions;
import com.spring.jwt.Interfaces.UserService;
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
    public ResponseEntity<ResponseAllUsersDto> getAllUsers(@RequestParam int pageNo){

        try {
            List<UserProfileDto> list= userService.getAllUsers(pageNo);
            ResponseAllUsersDto responseAllUsersDto = new ResponseAllUsersDto("success");
            responseAllUsersDto.setList(list);
            return ResponseEntity.status(HttpStatus.OK).body(responseAllUsersDto);
        } catch (UserNotFoundExceptions exception){
            ResponseAllUsersDto responseAllCarDto = new ResponseAllUsersDto("unsuccess");
            responseAllCarDto.setException("car not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseAllCarDto);
        }
        catch (PageNotFoundException exception){
            ResponseAllUsersDto responseAllCarDto = new ResponseAllUsersDto("unsuccess");
            responseAllCarDto.setException("page not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseAllCarDto);
        }
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<?> editUser(@RequestBody UserProfileDto userProfileDto, @PathVariable int id){

        try {
            BaseResponseDTO result = userService.editUser(userProfileDto,id);
            return ResponseEntity.status(HttpStatus.OK).body(new BaseResponseDTO("Successful",result.getMessage()));
        }catch (UserNotFoundExceptions exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseResponseDTO("Unsuccessful","user not found"));
        }
    }

    @RequestMapping(value = "/delete/{id}" ,method = RequestMethod.DELETE)
    public ResponseEntity<?> removeUser(@PathVariable int id){

        try {
            BaseResponseDTO result= userService.removeUser(id);
            return ResponseEntity.status(HttpStatus.OK).body(new BaseResponseDTO("Successful",result.getMessage()));
        }catch (UserNotFoundExceptions exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseResponseDTO("Unsuccessful","user not found"));
        }
    }

    @GetMapping("/getUser/{id}")
    public ResponseEntity<?> getUser(@PathVariable int id ){

        try {
            UserProfileDto userProfileDto = userService.getUser(id);
            return ResponseEntity.status(HttpStatus.OK).body(userProfileDto);
        }catch (UserNotFoundExceptions exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseResponseDTO("Unsuccessful","user not found"));

        }

    }

    @PutMapping("/changePassword/{id}")
    public ResponseEntity<BaseResponseDTO> changePassword(@PathVariable int id, @RequestBody PasswordChange passwordChange){

        try{
            BaseResponseDTO result =userService.changePassword(id,passwordChange);
            return ResponseEntity.status(HttpStatus.OK).body(new BaseResponseDTO("Successful",result.getMessage()));
        }catch (UserNotFoundExceptions exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseResponseDTO("Unsuccessfully","UserNotFoundException"));
        } catch (InvalidPasswordException exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseResponseDTO("Unsuccessfully","InvalidPasswordException"));
        }
    }



}
