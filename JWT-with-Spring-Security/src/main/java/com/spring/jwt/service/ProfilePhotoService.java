package com.spring.jwt.service;

public interface ProfilePhotoService {

    void addprofilephoto(byte[] data);
    byte[] getprofilePhotoData(int id);
    void updateprofilePhoto(int id, byte[] data);
    void deleteprofilePhoto(int id);

}
