package com.spring.jwt.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "biddingbuy")
public class Biddingbuy {
    @Id
    @Column(name = "bidding_buy_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int id;

    @Column(name = "final_cost")
    private int finalCost;

    @Column(name = "second2nd_entry")
    private int second2ndEntry;

    @Column(name = "last3rd_entry")
    private int last3rdEntry;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bidding_bidding_id", nullable = false)
    private Bidding biddingBidding;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dealer_vendor_id", nullable = false)
    private Dealer dealerVendor;

}