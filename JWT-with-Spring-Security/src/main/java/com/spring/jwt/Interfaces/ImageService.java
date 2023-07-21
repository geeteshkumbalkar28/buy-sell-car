package com.spring.jwt.Interfaces;

import com.spring.jwt.dto.ImageUploadDto;
import com.spring.jwt.dto.PhotoResopnseDto;
import com.spring.jwt.entity.CarPhoto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {

    String uploadFile(MultipartFile multipartFile) throws IOException;

    CarPhoto saveLink(String imageUrl, String type, int carId) throws IOException;

    PhotoResopnseDto findById(int id);

    ImageUploadDto deleteImage(int id,String imageName) throws IOException;

    ImageUploadDto updateImage(int id, String imageUrl);
}
