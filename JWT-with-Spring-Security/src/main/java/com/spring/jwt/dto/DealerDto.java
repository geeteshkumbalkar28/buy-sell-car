package com.spring.jwt.dto;

import com.spring.jwt.entity.Dealer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DealerDto {
    public String address;
    public int document;
    public String area;
    public String city;
    public String firstName;
    public String lastName;
    public String mobileNo;
    public String shopName;
    public String email;
    public String password;
    private Integer dealer_id;

    public DealerDto() {
    }

    public DealerDto(Dealer dealer) {
        this.address = dealer.getAddress();
        this.document = dealer.getDocument();
        this.area = dealer.getArea();
        this.city =dealer.getCity();
        this.firstName = dealer.getLastName();
        this.lastName =dealer.getLastName();
        this.mobileNo = dealer.getMobileNo();
        this.shopName = dealer.getShopName();
        this.email = dealer.getEmail();

        this.dealer_id = dealer.getId();
    }
}
