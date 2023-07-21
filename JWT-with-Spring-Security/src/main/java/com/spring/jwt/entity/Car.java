package com.spring.jwt.entity;

import com.spring.jwt.dto.CarDto;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "car")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_id", nullable = false)
    private int id;

    @Column(name = "ac_feature")
    private Boolean acFeature;

    @Column(name = "music_feature")
    private Boolean musicFeature;

    @Column(name = "area", length = 45)
    private String area;

    @Column(name = "`body type`", length = 45)
    private String bodyType;

    @Column(name = "brand", nullable = false, length = 45)
    private String brand;

    @Column(name = "car_insurance")
    private Boolean carInsurance;

    @Enumerated(EnumType.STRING)
    private Status carStatus;

    @Column(name = "city", length = 50)
    private String city;

    @Column(name = "color", length = 45)
    private String color;

    @Column(name = "description", length = 250)
    private String description;

    @Column(name = "fuel_type", length = 45)
    private String fuelType;

    @Column(name = "km_driven", length = 50)
    private int kmDriven;

    @Column(name = "model", length = 45)
    private String model;

    @Column(name = "no_of_wheels")
    private int noOfWheels;

    @Column(name = "owner_serial")
    private int ownerSerial;

    @Column(name = "power_window_feature")
    private Boolean powerWindowFeature;

    @Column(name = "price", length = 45)
    private int price;

    @Column(name = "rear_parking_camera_feature")
    private Boolean rearParkingCameraFeature;

    @Column(name = "registration", length = 45)
    private String registration;

    @Column(name = "safety_description", length = 250)
    private String safetyDescription;

    @Column(name = "transmission", length = 45)
    private String transmission;

    @Column(name = "tyre", length = 45)
    private String tyre;

    @Column(name = "year")
    private int year;

    @Column(name = "date")
    private LocalDate date;


//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "dealer_vendor_id", nullable = false)
//    private Dealer dealerVendor;
       @Column(name = "dealer_id")
       private int dealerId;
    private long carPhotoId;

//    private Carphoto carphotoCarPhoto;
@ManyToMany
@JoinTable(name = "dealer_bidding", joinColumns = @JoinColumn(name = "dealer_id"),
        inverseJoinColumns = @JoinColumn(name = "bidding_id"))
private Set<Bidding> biddings = new LinkedHashSet<>();


    @OneToMany(mappedBy = "carCar")
    private Set<PendingBooking> pendingBookings = new LinkedHashSet<>();
    public Car(CarDto carDto){
        this.acFeature = carDto.getAcFeature();
        this.date=carDto.getDate();
        this.musicFeature =carDto.getMusicFeature();
        this.area =carDto.getArea();
        this.bodyType = carDto.getBodyType();
        this.brand = carDto.getBrand();
        this.carInsurance = carDto.getCarInsurance();
        this.carStatus = carDto.getCarStatus();
        this.city = carDto.getCity();
        this.color = carDto.getColor();
        this.description =carDto.getDescription();
        this.fuelType = carDto.getFuelType();
        this.kmDriven = carDto.getKmDriven();
        this.model = carDto.getModel();
        this.noOfWheels = carDto.getNoOfWheels();
        this.ownerSerial = carDto.getOwnerSerial();
        this.powerWindowFeature = carDto.getPowerWindowFeature();
        this.price =carDto.getPrice();
        this.rearParkingCameraFeature = carDto.getRearParkingCameraFeature();
        this.registration = carDto.getRegistration();
        this.safetyDescription = carDto.getSafetyDescription();
        this.transmission = carDto.getTransmission();
        this.tyre = carDto.getTyre();
        this.year = carDto.getYear();
        this.dealerId=carDto.getDealer_id();
    }

}