package com.spring.jwt.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FilterDto {
    private Integer minPrice;
    private Integer maxPrice;
    private String area;
    private String brand;
    private String model;
    private String transmission;
    private String fuelType;
    private int year;
}
