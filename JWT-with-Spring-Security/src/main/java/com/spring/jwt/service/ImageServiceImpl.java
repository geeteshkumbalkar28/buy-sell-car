package com.spring.jwt.service;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.spring.jwt.dto.ImageUploadDto;
import com.spring.jwt.dto.PhotoResopnseDto;
import com.spring.jwt.entity.CarPhoto;
import com.spring.jwt.exception.CarNotFoundException;
import com.spring.jwt.exception.NoImageFoundException;
import com.spring.jwt.repository.ImageRepository;
import com.spring.jwt.Interfaces.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    @Autowired
    private final Cloudinary cloudinary;

    @Autowired
    private ImageRepository imageRepository;

    @Override
    public String uploadFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()){
            throw new IOException();
        }
        return cloudinary.uploader()
                .upload(multipartFile.getBytes(),
                        ObjectUtils.asMap("public_id", multipartFile.getOriginalFilename()))
                .get("url")
                .toString();
    }

    @Override
    public CarPhoto saveLink(String imageUrl, String type, int carId) throws IOException {

        if (imageUrl!= null){
            if (type != null){
              // Optional<Carphoto> car= imageRepository.findById(carId);
                //if (car.isPresent()){
                    CarPhoto image = new CarPhoto();
                    image.setPhoto_link(imageUrl);
                    image.setCar_id(carId);
                    image.setType(type);
                   return imageRepository.save(image);
                /*}else {
                    throw new CarNotFoundException("No car found with this id");
                }*/
            }
        }
        throw new IOException("Something went wrong");
    }

    @Override
    public PhotoResopnseDto findById(int car_photo_id) {
        Optional<CarPhoto> optionalImage = imageRepository.findById(car_photo_id);
        if (optionalImage.isPresent()) {
            String link = optionalImage.get().getPhoto_link();
            PhotoResopnseDto photoResopnseDto = new PhotoResopnseDto(link,optionalImage.get().getType(),optionalImage.get().getId(),optionalImage.get().getCar_id());
            return photoResopnseDto;
        }else {
            throw new NoImageFoundException("Image not found", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ImageUploadDto deleteImage(int id,String imageName) throws IOException {
        Optional<CarPhoto> carPhoto= imageRepository.findById(id);
        if (carPhoto.isPresent()){
            imageRepository.deleteById(carPhoto.get().getId());
            cloudinary.uploader().destroy(imageName,ObjectUtils.emptyMap());
            ImageUploadDto imageUploadDto= new ImageUploadDto();
            imageUploadDto.setMessage("Successful");
            return imageUploadDto;
        }else {
           throw new CarNotFoundException();
        }
    }

    @Override
    public ImageUploadDto updateImage(int id, String imageUrl) {
        Optional<CarPhoto> photo=imageRepository.findById(id);
        if (photo.isPresent()){
            photo.get().setPhoto_link(imageUrl);
            ImageUploadDto imageUploadDto= new ImageUploadDto();
            imageUploadDto.setMessage("Successful");
            imageRepository.save(photo.get());
            return imageUploadDto;
        }else {
            throw new CarNotFoundException();
        }
    }
}