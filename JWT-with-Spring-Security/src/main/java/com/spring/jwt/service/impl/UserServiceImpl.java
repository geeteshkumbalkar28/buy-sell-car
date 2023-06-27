package com.spring.jwt.service.impl;

import com.spring.jwt.dto.RegisterDto;
import com.spring.jwt.dto.UserDTO;
import com.spring.jwt.entity.Dealer;
import com.spring.jwt.entity.Role;
import com.spring.jwt.entity.User;
import com.spring.jwt.entity.Userprofile;
import com.spring.jwt.exception.BaseException;
import com.spring.jwt.repository.DealerRepository;
import com.spring.jwt.repository.RoleRepository;
import com.spring.jwt.repository.UserRepository;
import com.spring.jwt.service.UserService;
import com.spring.jwt.utils.BaseResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

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
        } catch (Exception e) {
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
}
