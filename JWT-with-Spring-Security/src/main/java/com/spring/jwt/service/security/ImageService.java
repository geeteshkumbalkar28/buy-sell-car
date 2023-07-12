package com.spring.jwt.service.security;

import com.spring.jwt.dto.PhotoDto;
import com.spring.jwt.dto.PhotoResopnseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {

    String uploadFile(MultipartFile multipartFile) throws IOException;

    void saveLink(String imageUrl, String type,int carId);

    PhotoResopnseDto findById(int id);
}
