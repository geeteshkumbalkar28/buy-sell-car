package com.spring.jwt.dto;


import com.spring.jwt.entity.Car;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarDto {

    private Boolean acFeature;

    private Boolean musicFeature;

    private String area;

    private String bodyType;

    private String brand;

    private Boolean carInsurance;

    private String carStatus;

    private String city;

    private String color;

    private String description;

    private String fuelType;

    private int kmDriven;

    private String model;

    private int noOfWheels;

    private int ownerSerial;

    private Boolean powerWindowFeature;

    private int price;

    private Boolean rearParkingCameraFeature;

    private String registration;

    private String safetyDescription;

    private String transmission;

    private String tyre;

    private int year;

    private int dealer_id;

//    private Dealer dealerVendor;
//
//    private Carphoto carphotoCarPhoto;


    public CarDto(Car car){
        this.acFeature = car.getAcFeature();
        this.musicFeature = car.getMusicFeature();
        this.area = car.getArea();
        this.bodyType =car.getBodyType();
        this.brand = car.getBrand();
        this.carInsurance = car.getCarInsurance();
        this.carStatus = car.getCarStatus();
        this.city = car.getCity();
        this.color = car.getColor();
        this.description = car.getDescription();
        this.fuelType =car.getFuelType();
        this.kmDriven = car.getKmDriven();
        this.model=car.getModel();
        this.noOfWheels = car.getNoOfWheels();
        this.ownerSerial = car.getOwnerSerial();
        this.powerWindowFeature = car.getPowerWindowFeature();
        this.price = car.getPrice();
        this.rearParkingCameraFeature = car.getRearParkingCameraFeature();
        this.registration = car.getRegistration();
        this.safetyDescription = car.getSafetyDescription();
        this.transmission = car.getTransmission();
        this.tyre = car.getTyre();
        this.year = car.getYear();
    }
}
