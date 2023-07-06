package com.spring.jwt.service.impl;

import com.spring.jwt.entity.ProfilePhoto;
import com.spring.jwt.repository.ProfilePhotoRepo;
import com.spring.jwt.service.ProfilePhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfilePhotoServiceImpl implements ProfilePhotoService {
@Autowired
private final ProfilePhotoRepo profilePhotoRepo;
      @Autowired
      public ProfilePhotoServiceImpl(ProfilePhotoRepo profilePhotoRepo)
        {
            this.profilePhotoRepo = profilePhotoRepo;
        }
        @Override
        public void addprofilephoto(byte[] data){
            ProfilePhoto profilePhoto = new ProfilePhoto();
            profilePhoto.setProfilephoto(data);
            profilePhotoRepo.save(profilePhoto);
        }



    @Override
    public byte[] getprofilePhotoData(int id) {
        ProfilePhoto profilePhoto = profilePhotoRepo.findById(id).orElse(null);

        if (profilePhoto != null) {
            return profilePhoto.getProfilephoto();
        } else {
            return null;
        }
    }


    @Override
    public void updateprofilePhoto(int id, byte[] data) {
        ProfilePhoto profilePhoto = profilePhotoRepo.findById(id).orElse(null);
        if (profilePhoto != null) {
            // Update the photo data
            profilePhoto.setProfilephoto(data);

            // Save the updated photo to the database
            profilePhotoRepo.save(profilePhoto);
        }

    }

    @Override
    public void deleteprofilePhoto(int id) {
        profilePhotoRepo.deleteById(id);
    }
}
