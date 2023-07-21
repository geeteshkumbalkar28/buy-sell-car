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
import com.spring.jwt.entity.Dealer;
import com.spring.jwt.exception.DealerNotFoundException;
import com.spring.jwt.repository.DealerRepository;
import com.spring.jwt.Interfaces.IDealerPhoto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/dealerDocument")
public class DealerPhotoController {
    @Autowired
    private IDealerPhoto iDealerPhoto;
    @Autowired
    private DealerRepository dealerRepository;

    public DealerPhotoController(IDealerPhoto iDealerPhoto) {
        this.iDealerPhoto =iDealerPhoto;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addPhoto(@RequestParam("file") MultipartFile file,@RequestParam int dealerId) {
        if (!file.isEmpty()) {
            try {
                long dealerPhotoId= iDealerPhoto.addphoto(file.getBytes());
                iDealerPhoto.setDealerPhotoIdInCar(dealerId,dealerPhotoId);

                return ResponseEntity.ok("Photo added");
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().body("Failed to add photo");
            }
        }
        return ResponseEntity.badRequest().body("Empty file");
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<byte[]> getPhoto(@PathVariable("id") int dealerId) {
        try {
            Optional<Dealer> dealer=dealerRepository.findById(dealerId);
            if(dealer.isEmpty()){
                throw new DealerNotFoundException("dealer not found");
            }
            byte[] photoData = iDealerPhoto.getPhotoData(dealer.get().getDealerDocumentPhoto());

            if (photoData != null) {
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(photoData);
            } else {
                return ResponseEntity.notFound().build();
            }
        }catch (DealerNotFoundException dealerNotFoundException){
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
                iDealerPhoto.updatePhoto(id, file.getBytes());
                return ResponseEntity.ok("Photo updated");
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().body("Failed to update photo");
            }
        }
        return ResponseEntity.badRequest().body("Empty file");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePhoto(@PathVariable("id") Long id,@RequestParam int dealerId) {
        try {
            iDealerPhoto.deletePhoto(id,dealerId);
            return ResponseEntity.ok("Photo deleted");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Failed to delete photo");
        }
    }
}

