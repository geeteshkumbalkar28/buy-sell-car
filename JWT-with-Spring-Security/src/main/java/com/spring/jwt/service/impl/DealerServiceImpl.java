package com.spring.jwt.service.impl;

import com.spring.jwt.dto.ChangePasswordDto;
import com.spring.jwt.dto.DealerDto;
import com.spring.jwt.dto.RegisterDto;
import com.spring.jwt.entity.Dealer;
import com.spring.jwt.entity.User;
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
                } else {
                    response.setCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
                    response.setMessage("Dealer details not found");
                }
            } else {
                response.setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
                response.setMessage("User is not a dealer");
            }
        } else {
            response.setCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
            response.setMessage("User not found");
        }

        return response;
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
        userRepository.save(user); // Save the updated User entity
    }
    @Override
    public List<DealerDto> getAllDealers() {
        List<Dealer> dealers = dealerRepository.findAll();
        return dealers.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public DealerDto getDealerById(Integer dealerId) {
        Optional<Dealer> dealerOptional = dealerRepository.findById(dealerId);
        return dealerOptional.map(this::convertToDto).orElse(null);
    }

    private DealerDto convertToDto(Dealer dealer) {
        DealerDto dealerDto = new DealerDto();
        dealerDto.setAddress(dealer.getAddress());
        dealerDto.setAdharShopact(dealer.getAdharShopact());
        dealerDto.setArea(dealer.getArea());
        dealerDto.setCity(dealer.getCity());
        dealerDto.setFirstname(dealer.getFirstname());
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

        dealerRepository.findById(dealerId).ifPresent(dealer -> {
            userRepository.deleteById(dealer.getUser().getId());
            dealerRepository.delete(dealer);
        });

        response.setCode(String.valueOf(HttpStatus.OK.value()));
        response.setMessage("Dealer deleted successfully");
        return response;
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
                        response.setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
                        response.setMessage("New password and confirm password do not match");
                    }
                } else {
                    response.setCode(String.valueOf(HttpStatus.UNAUTHORIZED.value()));
                    response.setMessage("Invalid old password");
                }
            } else {
                response.setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
                response.setMessage("User is not a dealer");
            }
        } else {
            response.setCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
            response.setMessage("User not found");
        }

        return response;
    }

}

