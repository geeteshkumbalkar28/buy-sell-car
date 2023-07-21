package com.spring.jwt.controller.file;
//
//import com.spring.jwt.Interfaces.CarPhotoService;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//@RestController
//@RequestMapping("/photo")
//public class CarPhotoController {
//    private final CarPhotoService carPhotoService;
//    public CarPhotoController(CarPhotoService carPhotoService)
//    {
//        this.carPhotoService =carPhotoService;
//    }
//
//    @PostMapping("/add")
//    public ResponseEntity<String> addPhoto(@RequestParam("file")MultipartFile file)
//    {
//        if (!file.isEmpty())
//        {
//            try {
//                carPhotoService.addphoto(file.getBytes());
//                return ResponseEntity.ok("photo added");
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//                return ResponseEntity.badRequest().body("Failed");
//
//            }
//
//        }
//        return ResponseEntity.badRequest().body("Empty file");
//
//    }
//
//    @GetMapping("/get/{id}")
//    public ResponseEntity<byte[]> getPhoto(@PathVariable("id") Long id) {
//        try {
//            byte[] photoData = carPhotoService.getPhotoData(id);
//
//            if (photoData != null) {
//                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(photoData);
//            } else {
//                return ResponseEntity.notFound().build();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.badRequest().body("Failed to retrieve photo".getBytes());
//        }
//    }
//
//
//    @PutMapping("/edit/{id}")
//    public ResponseEntity<String> updatePhoto(
//            @PathVariable("id") Long id,
//            @RequestParam("file") MultipartFile file) {
//        if (!file.isEmpty()) {
//            try {
//                carPhotoService.updatePhoto(id, file.getBytes());
//                return ResponseEntity.ok("Photo updated");
//            } catch (Exception e) {
//                e.printStackTrace();
//                return ResponseEntity.badRequest().body("Failed to update photo");
//            }
//        }
//        return ResponseEntity.badRequest().body("Empty file");
//    }
//
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<String> deletePhoto(@PathVariable("id") Long id) {
//        try {
//            carPhotoService.deletePhoto(id);
//            return ResponseEntity.ok("Photo deleted");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.badRequest().body("Failed to delete photo");
//        }
//        }
//}

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.spring.jwt.dto.ImageUploadDto;
import com.spring.jwt.entity.Car;
import com.spring.jwt.exception.CarNotFoundException;
import com.spring.jwt.exception.NoImageFoundException;
import com.spring.jwt.repository.CarRepo;
import com.spring.jwt.Interfaces.CarPhotoService;
import com.spring.jwt.Interfaces.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/photo")
public class CarPhotoController {
    private final CarPhotoService carPhotoService;
    @Autowired
    private CarRepo carRepo;

    @Autowired
    private  Cloudinary cloudinary;

    @Autowired
    private ImageService imageService;

    @RequestMapping("/")
    public String home() {
        return "home";
    }


    public CarPhotoController(CarPhotoService carPhotoService) {
        this.carPhotoService = carPhotoService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addPhoto(@RequestParam("file") MultipartFile file, @RequestParam int carId) {
        if (!file.isEmpty()) {
            try {
                long carPhotoId = carPhotoService.addphoto(file.getBytes());
                carPhotoService.setCarPhotoIdInCar(carId, carPhotoId);

                return ResponseEntity.ok("Photo added");
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().body("Failed to add photo");
            }
        }
        return ResponseEntity.badRequest().body("Empty file");
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<byte[]> getPhoto(@PathVariable("id") int carId) {
        try {
            Optional<Car> car = carRepo.findById(carId);
            if (car.isEmpty()) {
                throw new CarNotFoundException();
            }
            byte[] photoData = carPhotoService.getPhotoData(car.get().getCarPhotoId());

            if (photoData != null) {
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(photoData);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (CarNotFoundException carNotFoundException) {
            return ResponseEntity.badRequest().body(new byte[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new byte[0]);
        }
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<String> updatePhoto(
            @PathVariable("id") Long id,
            @RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                carPhotoService.updatePhoto(id, file.getBytes());
                return ResponseEntity.ok("Photo updated");
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().body("Failed to update photo");
            }
        }
        return ResponseEntity.badRequest().body("Empty file");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePhoto(@PathVariable("id") Long id, int carId) {
        try {
            carPhotoService.deletePhoto(id, carId);
            return ResponseEntity.ok("Photo deleted");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Failed to delete photo");
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("image") MultipartFile multipartFile, @RequestParam String type, @RequestParam int carId) throws IOException {
        try {
            String imageUrl = imageService.uploadFile(multipartFile);
            imageService.saveLink(imageUrl, type, carId);
            ImageUploadDto imageUploadDto= new ImageUploadDto();
            imageUploadDto.setMessage("Successful");
            return ResponseEntity.status(HttpStatus.OK).body(imageUploadDto);
        } catch (CarNotFoundException e) {
            ImageUploadDto imageUploadDto= new ImageUploadDto();
            imageUploadDto.setMessage("Unsuccessful");
            imageUploadDto.setException("Car not found with this id");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(imageUploadDto);
        }catch (IOException e){
            ImageUploadDto imageUploadDto= new ImageUploadDto();
            imageUploadDto.setMessage("Unsuccessful");
            imageUploadDto.setException("Something went wrong");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(imageUploadDto);
        }
    }

    @GetMapping("/getImage")
    public ResponseEntity<?> getImage(@RequestParam int carId) {
        System.out.println(carId);
        try {
            return ResponseEntity.status(HttpStatus.OK).body(imageService.findById(carId));
        } catch (NoImageFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No image found");
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteImage( @RequestParam int id,@RequestParam String imageName) throws IOException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(imageService.deleteImage(id,imageName));
        }catch (CarNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car not found with this id");
        }
    }

    @PutMapping("/updateImage")
    public ResponseEntity<?> updateImage(@RequestParam("image") MultipartFile multipartFile,@RequestParam int id,@RequestParam String imageName) throws IOException {
        try {
            cloudinary.uploader().destroy(imageName, ObjectUtils.emptyMap());
            String imageUrl = imageService.uploadFile(multipartFile);
            return ResponseEntity.status(HttpStatus.OK).body(imageService.updateImage(id,imageUrl));
        }catch (CarNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car not found with this id");
        }
    }
}

