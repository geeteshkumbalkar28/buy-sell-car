package com.spring.jwt.entity;



import com.spring.jwt.dto.DealerDto;
import jakarta.persistence.*;
import lombok.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "dealer")
public class Dealer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Dealer_id")
    private Integer id;

    @Column(name = "address")
    private String address;

    @Column(name = "adhar_shopact", nullable = false, length = 250)
    private String adharShopact;

    @Column(name = "area", nullable = false, length = 45)
    private String area;

    @Column(name = "city", nullable = false, length = 45)
    private String city;

    @Column(name = "fristname", length = 45)
    private String fristname;

    @Column(name = "last_name", length = 45)
    private String lastName;

    @Column(name = "mobile_no", nullable = false, length = 45)
    private String mobileNo;

    @Column(name = "shop_name", nullable = false, length = 250)
    private String shopName;
    @Column(name = "Email",nullable = false)
    private String email;

    @OneToOne
    @JoinColumn(name = "user_user_id", referencedColumnName = "user_id", nullable = false)
    private User user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bidding_bidding_id")
    private Bidding biddingBidding;

    @OneToMany(mappedBy = "dealerVendor")
    private Set<Biddingbuy> biddingbuys = new LinkedHashSet<>();

//    @OneToMany(mappedBy = "dealerVendor")
//    private Set<Car> cars = new LinkedHashSet<>();

    public Dealer(DealerDto dealerDto) {
        this.address = dealerDto.address;
        this.adharShopact = dealerDto.adharShopact;
        this.area = dealerDto.area;
        this.city =dealerDto.city;
        this.fristname = dealerDto.fristname;
        this.lastName = dealerDto.lastName;
        this.mobileNo = dealerDto.mobileNo;
        this.shopName = dealerDto.shopName;
        this.email = dealerDto.email;
    }
}