package com.spring.jwt.service;

import com.spring.jwt.dto.RegisterDto;
import com.spring.jwt.dto.UserDTO;
import com.spring.jwt.utils.BaseResponseDTO;

public interface UserService {
    BaseResponseDTO registerAccount(RegisterDto registerDto);
}
