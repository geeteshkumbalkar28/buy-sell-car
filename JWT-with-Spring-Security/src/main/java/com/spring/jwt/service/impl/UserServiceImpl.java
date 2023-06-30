package com.spring.jwt.service.impl;

import com.spring.jwt.dto.*;
import com.spring.jwt.entity.Dealer;
import com.spring.jwt.entity.Role;
import com.spring.jwt.entity.User;
import com.spring.jwt.entity.Userprofile;
import com.spring.jwt.exception.*;
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
            response.setCode(String.valueOf(HttpStatus.OK.value()));
            response.setMessage("Create account successfully");
        } catch (UserAlreadyExistException e) {
            response.setCode(String.valueOf(HttpStatus.SERVICE_UNAVAILABLE.value()));
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
            dealer.setFristname(registerDto.getFirstName());
            dealer.setLastName(registerDto.getLastName());
            dealer.setMobileNo(registerDto.getMobileNo());
            dealer.setShopName(registerDto.getShopName());
            dealer.setEmail(registerDto.getEmail());

            user.setDealers(dealer);
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

                if (passwordEncoder.matches(passwordChange.getOldPassword(), user.getPassword())) {
                    System.out.println(user.getPassword());
                    if (passwordChange.getNewPassword().equals(passwordChange.getConfirmPassword())) {
                        user.setPassword(passwordEncoder.encode(passwordChange.getNewPassword()));
                        userRepository.save(user);
                        response.setCode(String.valueOf(HttpStatus.OK.value()));
                        response.setMessage("Password changed successfully");
                    } else {
                        response.setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
                        throw new InvalidPasswordException("New password and confirm password does not match");
                    }
                } else {
                    response.setCode(String.valueOf(HttpStatus.UNAUTHORIZED.value()));
                    throw new InvalidPasswordException("Invalid Password");
                }
        }else {

            throw new UserNotFoundException("No user found with this id");

        }
        return response;
    }

    @Override
    public List<UserProfileDto> getAllUsers(int pageNo) {
        List<Userprofile> listOfUsers = userProfileRepository.findAll();
        UserNotFoundException userNotFoundException;
        if((pageNo*10)>listOfUsers.size()-1){
            throw new PageNotFoundException("page not found");

        }
        if(listOfUsers.size()<=0){throw new UserNotFoundException("User not found",HttpStatus.NOT_FOUND);}
        System.out.println("list of de"+listOfUsers.size());
        List<UserProfileDto> listOfUserDto = new ArrayList<>();

        int pageStart=pageNo*25;
        int pageEnd=pageStart+25;
        int diff=(listOfUsers.size()) - pageStart;
        for(int counter=pageStart,i=1;counter<pageEnd;counter++,i++){
            if(pageStart>listOfUsers.size()){break;}

            System.out.println("*");
            UserProfileDto userProfileDto = new UserProfileDto(listOfUsers.get(counter));
            listOfUserDto.add(userProfileDto);
            if(diff == i){
                break;
            }
        }
        return listOfUserDto;

    }

    @Override
    public BaseResponseDTO editUser(UserProfileDto userProfileDto, int id) {

        BaseResponseDTO response = new BaseResponseDTO();

        Optional<Userprofile> user = userProfileRepository.findById(id);
        if(user.isPresent()){
            user.get().setFirstName(userProfileDto.getFirstName());
            user.get().setLastName(userProfileDto.getLastName());
            user.get().setAddress(userProfileDto.getAddress());
            user.get().getUser().setMobileNo(userProfileDto.getMobile_no());
            user.get().getUser().setEmail(userProfileDto.getEmail());
            user.get().setCity(userProfileDto.getCity());

            userProfileRepository.save(user.get());
            response.setCode(String.valueOf(HttpStatus.OK.value()));
            response.setMessage("User details edited successfully");
        }else {
            response.setCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
            throw new UserNotFoundException("No user found with this id");
        }
        return response;
    }

    @Override
    @Transactional
    public BaseResponseDTO removeUser(int id) {
        BaseResponseDTO response = new  BaseResponseDTO();

        Optional<Userprofile> dealer = userProfileRepository.findById(id);
        if(dealer.isPresent()){
                userProfileRepository.DeleteById(id);
            response.setCode(String.valueOf(HttpStatus.OK.value()));
            response.setMessage("User deleted successfully");
        }else{
            response.setCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
            throw new UserNotFoundException("No user found with this id");
        }
        return response;
    }

}
