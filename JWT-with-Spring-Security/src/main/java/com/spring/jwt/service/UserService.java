package com.spring.jwt.service;

import com.spring.jwt.dto.*;
import com.spring.jwt.utils.BaseResponseDTO;

import java.util.List;

public interface UserService {
    BaseResponseDTO registerAccount(RegisterDto registerDto);


    BaseResponseDTO changePassword(int id, PasswordChange passwordChange);


    String editUser(UserProfileDto userProfileDto, int id);

    String removeUser(int id);

    List<ResponseUserProfileDto> getAllUsers(int pageNo);
}
