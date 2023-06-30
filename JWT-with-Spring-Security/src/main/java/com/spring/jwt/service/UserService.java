package com.spring.jwt.service;

import com.spring.jwt.dto.*;
import com.spring.jwt.utils.BaseResponseDTO;

import java.util.List;

public interface UserService {
    BaseResponseDTO registerAccount(RegisterDto registerDto);


    BaseResponseDTO changePassword(int id, PasswordChange passwordChange);


    BaseResponseDTO editUser(UserProfileDto userProfileDto, int id);

    BaseResponseDTO removeUser(int id);

    List<UserProfileDto> getAllUsers(int pageNo);
}
