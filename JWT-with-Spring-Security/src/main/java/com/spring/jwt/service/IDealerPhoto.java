package com.spring.jwt.service;

public interface IDealerPhoto {
    public long addphoto(byte[] data);
    byte[] getPhotoData(Long id);
    void updatePhoto(Long id, byte[] data);
    void deletePhoto(Long id);

    public void setDealerPhotoIdInCar(int carId,long carPhotoId);
}
