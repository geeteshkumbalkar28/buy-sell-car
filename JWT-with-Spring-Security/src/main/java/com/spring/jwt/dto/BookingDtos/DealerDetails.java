package com.spring.jwt.dto.BookingDtos;

import com.spring.jwt.entity.Dealer;


public class DealerDetails {
    public String address;
    public int document;
    public String area;
    public String city;
    public String firstName;
    public String lastName;
    public String mobileNo;
    public String shopName;
    public String email;
    private Integer dealer_id;
    private Integer userId;
    private Boolean status;

    public DealerDetails() {
    }

    public DealerDetails(Dealer dealer) {
        this.address = dealer.getAddress();
        this.status=dealer.getStatus();
        this.area = dealer.getArea();
        this.city =dealer.getCity();
        this.firstName = dealer.getFirstname();
        this.lastName =dealer.getLastName();
        this.mobileNo = dealer.getMobileNo();
        this.shopName = dealer.getShopName();
        this.email = dealer.getEmail();

        this.dealer_id = dealer.getId();
        this.userId = dealer.getUser().getId();

    }
}
