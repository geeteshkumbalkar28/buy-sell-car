package com.spring.jwt.dto;

import lombok.Data;

@Data
public class PhotoResopnseDto {


    private String imageLink;
    private String type;
    private int carId;
    private int carPhotoId;

    public PhotoResopnseDto(String imageLink, String type,int carPhotoId,int carId) {
        this.imageLink = imageLink;
        this.type = type;
        this.carPhotoId=carPhotoId;
        this.carId=carId;
    }
}
