package com.spring.jwt.controller.file;
//
//import com.spring.jwt.service.CarPhotoService;
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
import com.spring.jwt.dto.PhotoResopnseDto;
import com.spring.jwt.entity.Car;
import com.spring.jwt.exception.CarNotFoundException;
import com.spring.jwt.exception.NoImageFoundException;
import com.spring.jwt.repository.CarRepo;
import com.spring.jwt.service.CarPhotoService;
import com.spring.jwt.service.security.ImageService;
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
    private ImageService imageService;

    @RequestMapping("/")
    public String home(){
        return "home";
    }


    public CarPhotoController(CarPhotoService carPhotoService) {
        this.carPhotoService = carPhotoService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addPhoto(@RequestParam("file") MultipartFile file,@RequestParam int carId) {
        if (!file.isEmpty()) {
            try {
                long carPhotoId= carPhotoService.addphoto(file.getBytes());
                carPhotoService.setCarPhotoIdInCar(carId,carPhotoId);

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
            Optional<Car> car =carRepo.findById(carId);
            if(car.isEmpty()){
                throw new CarNotFoundException();
            }
            byte[] photoData = carPhotoService.getPhotoData(car.get().getCarPhotoId());

            if (photoData != null) {
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(photoData);
            } else {
                return ResponseEntity.notFound().build();
            }
        }catch (CarNotFoundException carNotFoundException){
            return ResponseEntity.badRequest().body(new byte[0]);

        }
        catch (Exception e) {
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
    public ResponseEntity<String> deletePhoto(@PathVariable("id") Long id,int carId) {
        try {
            carPhotoService.deletePhoto(id,carId);
            return ResponseEntity.ok("Photo deleted");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Failed to delete photo");
        }
    }




    @PostMapping("/upload")
    public String uploadFile(@RequestParam("image") MultipartFile multipartFile,@RequestParam String type,@RequestParam int carId) throws IOException {
        String imageUrl = imageService.uploadFile(multipartFile);
        if (imageUrl != null){
            imageService.saveLink(imageUrl,type,carId);
            return "image saved";
        }else {
            return "Something went wrong";
        }
    }

    @GetMapping("/getImage")
    public ResponseEntity<?> getImage(@RequestParam int carId){
        System.out.println(carId);
        try {
            return ResponseEntity.status(HttpStatus.OK).body(imageService.findById(carId));
        }catch (NoImageFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no");
        }
    }
}

