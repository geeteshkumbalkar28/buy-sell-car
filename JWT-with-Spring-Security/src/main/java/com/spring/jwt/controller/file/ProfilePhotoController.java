package com.spring.jwt.controller.file;

import com.spring.jwt.entity.User;
import com.spring.jwt.exception.UserNotDealerException;
import com.spring.jwt.exception.UserNotFoundExceptions;
import com.spring.jwt.repository.UserRepository;
import com.spring.jwt.Interfaces.ProfilePhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/userProfilePhoto")
public class ProfilePhotoController {
    private final ProfilePhotoService carPhotoService;
    @Autowired
    private UserRepository userRepository;

    public ProfilePhotoController(ProfilePhotoService carPhotoService) {
        this.carPhotoService = carPhotoService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addPhoto(@RequestParam("file") MultipartFile file, @RequestParam int userId) {
        if (!file.isEmpty()) {
            try {
                long profilePhotoId= carPhotoService.addprofilephoto(file.getBytes());
                carPhotoService.setUserPhotoID(userId,profilePhotoId);

                return ResponseEntity.ok("Profile photo added");
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().body("Failed to add Profile photo");
            }
        }
        return ResponseEntity.badRequest().body("Profile photo Empty");
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<byte[]> getPhoto(@PathVariable("id") int userId) {
        try {
            Optional<User> user=userRepository.findById(userId);
            if(user.isEmpty()){
                throw new UserNotDealerException("user not found");
            }
            byte[] photoData = carPhotoService.getprofilePhotoData(user.get().getProfilePhotoId());

            if (photoData != null) {
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(photoData);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (UserNotFoundExceptions userNotFoundExceptions){
            return ResponseEntity.badRequest().body(new byte[0]);

        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new byte[0]);
        }
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<String> updatePhoto(
            @PathVariable("profilePhotoId") Long profilePhotoId,
            @RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                carPhotoService.updateprofilePhoto(profilePhotoId, file.getBytes());
                return ResponseEntity.ok(" Profile Photo updated");
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().body("Failed to  update Profile photo");
            }
        }
        return ResponseEntity.badRequest().body("Profile photo Empty");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePhoto(@PathVariable("id") Long id) {
        try {
            carPhotoService.deleteprofilePhoto(id);
            return ResponseEntity.ok(" Profile photo deleted");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Failed to delete Profile photo");
        }
    }
}
