package com.spring.jwt.Interfaces;

public interface IDealerPhoto {
    public long addphoto(byte[] data);
    public byte[] getPhotoData(Long id);
    public void updatePhoto(Long id, byte[] data);
    public void deletePhoto(Long id,int dealerId);

    public void setDealerPhotoIdInCar(int carId,long carPhotoId);
}
