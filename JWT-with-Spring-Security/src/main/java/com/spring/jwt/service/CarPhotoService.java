package com.spring.jwt.service;



public interface CarPhotoService {
    public long addphoto(byte[] data);
    byte[] getPhotoData(Long id);
    void updatePhoto(Long id, byte[] data);
    void deletePhoto(Long id);

    public void setCarPhotoIdInCar(int carId,long carPhotoId);

}
