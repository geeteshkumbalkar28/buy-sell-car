package com.spring.jwt.service;

import com.spring.jwt.entity.ProfilePhoto;
import com.spring.jwt.entity.User;
import com.spring.jwt.repository.ProfilePhotoRepo;
import com.spring.jwt.repository.UserRepository;
import com.spring.jwt.Interfaces.ProfilePhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfilePhotoServiceImpl implements ProfilePhotoService {
@Autowired
private  ProfilePhotoRepo profilePhotoRepo;
@Autowired
private UserRepository userRepository;

    @Override
    public Long addprofilephoto(byte[] data) {
        ProfilePhoto profilePhoto = new ProfilePhoto();
        profilePhoto.setProfilephoto(data);
        profilePhotoRepo.save(profilePhoto);
        return profilePhoto.getId();

    }

    @Override
    public byte[] getprofilePhotoData(Long id) {
        ProfilePhoto profilePhoto = profilePhotoRepo.findById(id).orElse(null);

        if (profilePhoto != null) {
            return profilePhoto.getProfilephoto();
        } else {
            return null;
        }
    }

    @Override
    public void updateprofilePhoto(Long id, byte[] data) {

        ProfilePhoto profilePhoto = profilePhotoRepo.findById(id).orElse(null);

        if (profilePhoto != null) {
            // Update the photo data
            profilePhoto.setProfilephoto(data);

            // Save the updated photo to the database
            profilePhotoRepo.save(profilePhoto);
        }

    }

    @Override
    public void deleteprofilePhoto(Long id)
    {
        profilePhotoRepo.deleteById(id);

    }

    @Override
    public void setUserPhotoID(int UserId, long profilePhotId) {
        Optional<User> dealer = userRepository.findById(UserId);
        dealer.get().setProfilePhotoId(profilePhotId);
        userRepository.save(dealer.get());

    }
}
