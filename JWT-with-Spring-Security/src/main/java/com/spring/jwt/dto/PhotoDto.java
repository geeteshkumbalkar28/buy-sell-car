package com.spring.jwt.dto;

public class PhotoDto {

    private String imageLink;
    private String type;

    private int carId;

    public PhotoDto(String imageLink, String type,int id) {
        this.imageLink = imageLink;
        this.type = type;
        this.carId=id;
    }
}
