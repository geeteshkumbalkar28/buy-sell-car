package com.spring.jwt.service;

import com.spring.jwt.dto.CarDto;
import com.spring.jwt.dto.FilterDto;
import com.spring.jwt.entity.Car;

import java.util.List;
import java.util.Optional;

public interface ICarRegister {
    public String AddCarDetails(CarDto carDto);
    public String editCarDetails(CarDto carDto,int id);
    public List<CarDto> getAllCarsWithPages(int PageNo);
    public String deleteCar(int id);


    public Optional<List<Car>> FindByArea(String area);

    public List<CarDto> searchByFilter(FilterDto filterDto, int pageNo);


    List<CarDto> getCarsByDealerIdWithStatus(int dealerId, String status);

}
