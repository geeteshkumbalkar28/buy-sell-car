package com.spring.jwt.service.impl;

import com.spring.jwt.dto.*;
import com.spring.jwt.entity.Dealer;
import com.spring.jwt.entity.Role;
import com.spring.jwt.entity.User;
import com.spring.jwt.entity.Userprofile;
import com.spring.jwt.exception.BaseException;
import com.spring.jwt.repository.DealerRepository;
import com.spring.jwt.repository.RoleRepository;
import com.spring.jwt.repository.UserProfileRepository;
import com.spring.jwt.repository.UserRepository;
import com.spring.jwt.service.UserService;
import com.spring.jwt.utils.BaseResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserProfileRepository userProfileRepository;

    private final RoleRepository roleRepository;

    private final DealerRepository dealerRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public BaseResponseDTO registerAccount(RegisterDto registerDto) {
        BaseResponseDTO response = new BaseResponseDTO();

        validateAccount(registerDto);

        User user = insertUser(registerDto);

        try {
            userRepository.save(user);
            response.setStatus("success");
            response.setCode("200");

            if (user.getRoles().stream().anyMatch(role -> role.getName().equals("DEALER"))) {
                response.setMessage(user.getEmail()+ " Dealer account created successfully");
            } else {
                response.setMessage(user.getEmail()+" account created successfully");
            }
        } catch (Exception e) {
            response.setCode("503");
            response.setStatus("unsuccessful");
            response.setMessage("Service unavailable");
        }

        return response;
    }

    private User insertUser(RegisterDto registerDto) {
        User user = new User();
        user.setEmail(registerDto.getEmail());
        user.setMobileNo(registerDto.getMobileNo());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName(registerDto.getRoles());
        roles.add(role);
        user.setRoles(roles);

        if (role.getName().equals("USER")) {
            Userprofile profile = new Userprofile();
            profile.setAddress(registerDto.getAddress());
            profile.setCity(registerDto.getCity());
            profile.setFirstName(registerDto.getFirstName());
            profile.setLastName(registerDto.getLastName());

            user.setProfile(profile);
            profile.setUser(user);
        } else if (role.getName().equals("DEALER")) {
            Dealer dealer = new Dealer();
            dealer.setAddress(registerDto.getAddress());
            dealer.setAdharShopact(registerDto.getAdharShopact());
            dealer.setArea(registerDto.getArea());
            dealer.setCity(registerDto.getCity());
            dealer.setFirstname(registerDto.getFirstName());
            dealer.setLastName(registerDto.getLastName());
            dealer.setMobileNo(registerDto.getMobileNo());
            dealer.setShopName(registerDto.getShopName());
            dealer.setEmail(registerDto.getEmail());

            user.setDealer(dealer);
            dealer.setUser(user); // Set the user instance in the dealer
        }

        return user;
    }

    private void validateAccount(RegisterDto registerDto) {
        // validate null data
        if (ObjectUtils.isEmpty(registerDto)) {
            throw new BaseException(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Data must not be empty");
        }

        // validate duplicate username
        User user = userRepository.findByEmail(registerDto.getEmail());
        if (!ObjectUtils.isEmpty(user)) {
            throw new BaseException(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Username already exists");
        }

        // validate role
        List<String> roles = roleRepository.findAll().stream().map(Role::getName).toList();
        if (!roles.contains(registerDto.getRoles())) {
            throw new BaseException(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Invalid role");
        }
    }

    @Override
    public BaseResponseDTO changePassword(int id, PasswordChange passwordChange) {
        BaseResponseDTO response= new BaseResponseDTO();

        Optional<User> userOptional= userRepository.findById(id);

        if (userOptional.isPresent()) {

            User user= userOptional.get();

            System.out.println(user);
            if (user != null) {
                if (passwordEncoder.matches(passwordChange.getOldPassword(), user.getPassword())) {
                    System.out.println(user.getPassword());
                    if (passwordChange.getNewPassword().equals(passwordChange.getConfirmPassword())) {
                        user.setPassword(passwordEncoder.encode(passwordChange.getNewPassword()));
                        userRepository.save(user);
                        response.setCode(String.valueOf(HttpStatus.OK.value()));
                        response.setMessage("Password changed successfully");
                    } else {
                        response.setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
                        response.setMessage("New password and confirm password does not match");                    }
                } else {
                    response.setCode(String.valueOf(HttpStatus.UNAUTHORIZED.value()));
                    response.setMessage("Invalid old password");
                }
            } else {
                response.setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
                response.setMessage("User not found");
            }
        }
        return response;
    }

    @Override
    public List<ResponseUserProfileDto> getAllUsers(int pageNo) {
        List<Userprofile> listOfUsers = userProfileRepository.findAll();
        System.out.println("list of User"+listOfUsers.size());
        List<ResponseUserProfileDto> listOfUserDto = new ArrayList<>();
//        System.out.println("2");
        int pageStart=pageNo*25;
        int pageEnd=pageStart+25;
        int diff=(listOfUsers.size()) - pageStart;

        for(int counter=pageStart,i=1;counter<pageEnd;counter++,i++){
            if(pageStart>listOfUsers.size()){break;}

            System.out.println("*");
            ResponseUserProfileDto responseUserDto = new ResponseUserProfileDto(listOfUsers.get(counter));

            listOfUserDto.add(responseUserDto);

            if(diff == i){
                break;
            }
        }

        System.out.println(listOfUserDto);
        return listOfUserDto;

    }

    @Override
    public String editUser(UserProfileDto userProfileDto, int id) {

        Optional<Userprofile> user = userProfileRepository.findById(id);
        if(user.isPresent()){
            user.get().setFirstName(userProfileDto.getFirstName());
            user.get().setLastName(userProfileDto.getLastName());
            user.get().setAddress(userProfileDto.getAddress());
            user.get().getUser().setMobileNo(userProfileDto.getMobile_no());
            user.get().getUser().setEmail(userProfileDto.getEmail());
            user.get().setCity(userProfileDto.getCity());

            userProfileRepository.save(user.get());
            return "User Details Edited of id No= :"+id;
        }
        return "invalid id";
    }

    @Override
    @Transactional
    public String removeUser(int id) {
        Optional<Userprofile> dealer = userProfileRepository.findById(id);
        if(dealer.isPresent()){
            try {
                userProfileRepository.DeleteById(id);
                return "User Delete Successfully :"+ id;
            }catch (Exception e){
                System.err.println(e);
            }

        }
        return "id invalid";
    }

}
