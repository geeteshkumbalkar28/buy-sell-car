package com.spring.jwt.service.impl;


import com.cloudinary.Cloudinary;
import com.spring.jwt.dto.PhotoDto;
import com.spring.jwt.dto.PhotoResopnseDto;
import com.spring.jwt.entity.Car;
import com.spring.jwt.entity.Carphoto;
import com.spring.jwt.exception.NoImageFoundException;
import com.spring.jwt.repository.ImageRepository;
import com.spring.jwt.service.security.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    @Autowired
    private final Cloudinary cloudinary;

    @Autowired
    private ImageRepository imageRepository;

    @Override
    public String uploadFile(MultipartFile multipartFile) throws IOException {
        return cloudinary.uploader()
                .upload(multipartFile.getBytes(),
                        Map.of("public_id", UUID.randomUUID().toString()))
                .get("url")
                .toString();
    }

    @Override
    public void saveLink(String imageUrl, String type,int carId) {
        Carphoto image = new Carphoto();
        image.setPhoto_link(imageUrl);
        image.setCar_id(carId);
        image.setType(type);

        imageRepository.save(image);

    }

    @Override
    public PhotoResopnseDto findById(int car_photo_id) {
        Optional<Carphoto> optionalImage = imageRepository.findById(car_photo_id);
        if (optionalImage.isPresent()) {
            String link = optionalImage.get().getPhoto_link();
            PhotoResopnseDto photoResopnseDto = new PhotoResopnseDto(link,optionalImage.get().getType(),optionalImage.get().getId(),optionalImage.get().getCar_id());
            return photoResopnseDto;
        }else {
            throw new NoImageFoundException("Image not found", HttpStatus.NOT_FOUND);
        }
    }
}