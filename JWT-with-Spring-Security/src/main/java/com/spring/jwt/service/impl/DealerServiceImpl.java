package com.spring.jwt.service.impl;

import com.spring.jwt.dto.ChangePasswordDto;
import com.spring.jwt.dto.DealerDto;
import com.spring.jwt.dto.RegisterDto;
import com.spring.jwt.entity.Dealer;
import com.spring.jwt.entity.User;
import com.spring.jwt.exception.*;
import com.spring.jwt.repository.DealerRepository;
import com.spring.jwt.repository.RoleRepository;
import com.spring.jwt.repository.UserRepository;
import com.spring.jwt.service.DealerService;
import com.spring.jwt.utils.BaseResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class DealerServiceImpl implements DealerService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DealerRepository dealerRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public BaseResponseDTO updateDealer(Integer userId, RegisterDto registerDto) {
        BaseResponseDTO response = new BaseResponseDTO();
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (user.getRoles().stream().anyMatch(role -> role.getName().equals("DEALER"))) {
                Dealer dealer = user.getDealer();
                if (dealer != null) {
                    updateDealerDetails(dealer, registerDto);
                    dealerRepository.save(dealer);
                    response.setCode(String.valueOf(HttpStatus.OK.value()));
                    response.setMessage("Dealer details updated successfully");
                    return response;

                } else {

                    throw new DealerDeatilsNotFoundException("Dealer details not found");
                }
            } else {


                throw new UserNotDealerException("User is not a dealer");
            }
        } else {

            throw new UserNotFoundException("User not found");

        }


    }

    private void updateDealerDetails(Dealer dealer, RegisterDto registerDto) {
        dealer.setAddress(registerDto.getAddress());
        dealer.setAdharShopact(registerDto.getAdharShopact());
        dealer.setArea(registerDto.getArea());
        dealer.setCity(registerDto.getCity());
        dealer.setFirstname(registerDto.getFirstName());
        dealer.setLastName(registerDto.getLastName());
        dealer.setMobileNo(registerDto.getMobileNo());
        dealer.setShopName(registerDto.getShopName());
        dealer.setEmail(registerDto.getEmail());

        User user = dealer.getUser();
        user.setEmail(registerDto.getEmail()); // Update email in User table as well
        user.setMobileNo(registerDto.getMobileNo());
        userRepository.save(user); // Save the updated User entity
    }
    @Override
    public List<DealerDto> getAllDealers() {
        List<Dealer> dealers = dealerRepository.findAll();
        if (dealers.size() < 0) {
            throw new DealerNotFoundException("Dealer not found");
        }

        return dealers.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public DealerDto getDealerById(Integer dealerId) {
        Optional<Dealer> dealerOptional = dealerRepository.findById(dealerId);
        if(dealerOptional.isEmpty())
        {
            throw new DealerNotFoundException("dealer not found by id");
        }
        return dealerOptional.map(this::convertToDto).orElse(null);
    }

    private DealerDto convertToDto(Dealer dealer) {
        DealerDto dealerDto = new DealerDto();
        dealerDto.setDealer_id(dealer.getId());
        dealerDto.setAddress(dealer.getAddress());
        dealerDto.setAdharShopact(dealer.getAdharShopact());
        dealerDto.setArea(dealer.getArea());
        dealerDto.setCity(dealer.getCity());
        dealerDto.setFirstName(dealer.getFirstname());
        dealerDto.setLastName(dealer.getLastName());
        dealerDto.setMobileNo(dealer.getMobileNo());
        dealerDto.setShopName(dealer.getShopName());
        dealerDto.setEmail(dealer.getEmail());
        return dealerDto;
    }

    @Override
    @Transactional
    public BaseResponseDTO deleteDealer(Integer dealerId) {
        BaseResponseDTO response = new BaseResponseDTO();

        Optional<Dealer> dealerOptional = dealerRepository.findById(dealerId);
        if (dealerOptional.isPresent()) {
            Dealer dealer = dealerOptional.get();
            User user = dealer.getUser();

            // Delete user roles associated with the dealer
            user.getRoles().clear();
            userRepository.save(user);

            // Delete the dealer
            dealerRepository.deleteById(dealerId);
            userRepository.delete(user);

            response.setCode(String.valueOf(HttpStatus.OK.value()));
            response.setMessage("Dealer deleted successfully");
            return response;
        } else {

            throw new DealerNotFoundException("Dealer not found");
        }


    }



    @Override
    public BaseResponseDTO changePassword(Integer userId, ChangePasswordDto changePasswordDto) {
        BaseResponseDTO response = new BaseResponseDTO();
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (user.getRoles().stream().anyMatch(role -> role.getName().equals("DEALER"))) {
                if (passwordEncoder.matches(changePasswordDto.getOldPassword(), user.getPassword())) {
                    if (changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmNewPassword())) {
                        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
                        userRepository.save(user);
                        response.setCode(String.valueOf(HttpStatus.OK.value()));
                        response.setMessage("Password changed successfully");
                    } else {
                        throw new NewAndOldPasswordDoseNotMatchException("New password and confirm password do not match");
                    }
                } else {

                    throw new InvalidOldPasswordException("Invalid old password");

                }
            } else {

                throw new UserNotDealerException("User is not a dealer");
            }
        } else {
            throw new UserNotFoundException("User not found");

        }

        return response;
    }

}

