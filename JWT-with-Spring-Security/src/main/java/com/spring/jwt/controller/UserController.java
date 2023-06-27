package com.spring.jwt.controller;

import com.spring.jwt.dto.PasswordChange;
import com.spring.jwt.dto.ResponseUserProfileDto;
import com.spring.jwt.dto.UserProfileDto;
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
    public List<ResponseUserProfileDto> getAllUsers(@RequestParam int pageNo){
        return userService.getAllUsers(pageNo);

    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<?> editUser(@RequestBody UserProfileDto userProfileDto, @PathVariable int id){
        //  userService.editUser(userProfileDto,id);
        return ResponseEntity.status(HttpStatus.OK).body(userService.editUser(userProfileDto,id));

    }

    @RequestMapping(value = "/delete/{id}" ,method = RequestMethod.DELETE)
    public ResponseEntity<?> removeUser(@PathVariable int id){

        return ResponseEntity.status(HttpStatus.OK).body(userService.removeUser(id));

    }

    @PostMapping("/changePassword/{id}")
    public ResponseEntity<BaseResponseDTO> changePassword(@PathVariable int id, @RequestBody PasswordChange passwordChange){

        return ResponseEntity.ok(userService.changePassword(id,passwordChange));
    }


}
