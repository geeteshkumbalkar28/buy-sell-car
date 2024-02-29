package com.spring.jwt.service;

import com.spring.jwt.entity.Car;
import com.spring.jwt.entity.Photo;
import com.spring.jwt.repository.CarRepo;
import com.spring.jwt.repository.PhotoRepo;
import com.spring.jwt.Interfaces.CarPhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CarPhotoServiceImpl implements CarPhotoService {

    private final PhotoRepo photoRepo;
    @Autowired
    private CarRepo carRepo;
      @Autowired
      public CarPhotoServiceImpl(PhotoRepo photoRepo)
        {
            this.photoRepo = photoRepo;
        }
        @Override
        public long addphoto(byte[] data){
            Photo photo = new Photo();
            photo.setPhoto1(data);
            photoRepo.save(photo);
            return photo.getId();
      }


    @Override
    public byte[] getPhotoData(Long id) {
        // Retrieve the photo entity from the database based on the provided ID
        Photo photo = photoRepo.findById(id).orElse(null);

        if (photo != null) {
            return photo.getPhoto1();
        } else {
            return null;
        }
    }

    @Override
    public void updatePhoto(Long id, byte[] data) {
        // Retrieve the photo entity from the database based on the provided ID
        Photo photo = photoRepo.findById(id).orElse(null);

        if (photo != null) {
            // Update the photo data
            photo.setPhoto1(data);

            // Save the updated photo to the database
            photoRepo.save(photo);
        }
    }

    @Override
    public void deletePhoto(Long id,int carId) {
        // Delete the photo from the database based on the provided ID
        Optional<Car> car = carRepo.findById(carId);
        car.get().setCarPhotoId(0);
        photoRepo.deleteById(id);
    }

    @Override
    public void setCarPhotoIdInCar(int carId,long carPhotoId){
        Optional<Car> car = carRepo.findById(carId);
        car.get().setCarPhotoId(carPhotoId);
        carRepo.save(car.get());
    }
}
