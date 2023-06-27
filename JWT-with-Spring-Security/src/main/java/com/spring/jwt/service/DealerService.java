package com.spring.jwt.service;

import com.spring.jwt.dto.ChangePasswordDto;
import com.spring.jwt.dto.DealerDto;
import com.spring.jwt.dto.RegisterDto;
import com.spring.jwt.utils.BaseResponseDTO;

import java.util.List;

public interface DealerService {
    BaseResponseDTO updateDealer(Integer userId, RegisterDto registerDto);

    List<DealerDto> getAllDealers();
    DealerDto getDealerById(Integer dealerId);

    BaseResponseDTO deleteDealer(Integer dealerId);

    BaseResponseDTO changePassword(Integer userId, ChangePasswordDto changePasswordDto);

}