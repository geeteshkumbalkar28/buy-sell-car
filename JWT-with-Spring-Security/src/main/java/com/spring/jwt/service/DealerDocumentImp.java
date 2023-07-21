package com.spring.jwt.service;

import com.spring.jwt.entity.Dealer;
import com.spring.jwt.entity.DocumentPhoto;
import com.spring.jwt.repository.DealerRepository;
import com.spring.jwt.repository.DocumentRepo;
import com.spring.jwt.Interfaces.IDealerPhoto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DealerDocumentImp implements IDealerPhoto {
    @Autowired
    private DocumentRepo photoRepo;
    @Autowired
    private DealerRepository dealerRepository;
    @Autowired
    public DealerDocumentImp(DocumentRepo photoRepo)
    {
        this.photoRepo = photoRepo;
    }
    @Override
    public long addphoto(byte[] data){
        DocumentPhoto photo = new DocumentPhoto();
        photo.setPhoto1(data);
        photoRepo.save(photo);
        return photo.getId();
    }


    @Override
    public byte[] getPhotoData(Long id) {
        // Retrieve the photo entity from the database based on the provided ID
        DocumentPhoto photo = photoRepo.findById(id).orElse(null);

        if (photo != null) {
            return photo.getPhoto1();
        } else {
            return null;
        }
    }

    @Override
    public void updatePhoto(Long id, byte[] data) {
        // Retrieve the photo entity from the database based on the provided ID
        DocumentPhoto photo = photoRepo.findById(id).orElse(null);

        if (photo != null) {
            // Update the photo data
            photo.setPhoto1(data);

            // Save the updated photo to the database
            photoRepo.save(photo);
        }
    }

    @Override
    public void deletePhoto(Long id,int dealerId) {
        // Delete the photo from the database based on the provided ID
        Optional<Dealer> dealer = dealerRepository.findById(dealerId);
        dealer.get().setDealerDocumentPhoto(0);
        photoRepo.deleteById(id);
    }

    @Override
    public void setDealerPhotoIdInCar(int dealerId,long dealerPhotoId){
        Optional<Dealer> dealer = dealerRepository.findById(dealerId);
        dealer.get().setDealerDocumentPhoto(dealerPhotoId);
        dealerRepository.save(dealer.get());
    }
}
