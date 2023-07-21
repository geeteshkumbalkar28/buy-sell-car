package com.spring.jwt.Interfaces;



public interface CarPhotoService {
    public long addphoto(byte[] data);
    byte[] getPhotoData(Long id);
    void updatePhoto(Long id, byte[] data);
    void deletePhoto(Long id,int carId);

    public void setCarPhotoIdInCar(int carId,long carPhotoId);

}
