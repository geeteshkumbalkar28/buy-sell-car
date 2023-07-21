package com.spring.jwt.Interfaces;

import com.spring.jwt.dto.CarDto;
import com.spring.jwt.dto.FilterDto;

import java.util.List;

public interface ICarRegister {
    public String AddCarDetails(CarDto carDto);
    public String editCarDetails(CarDto carDto,int id);
    public List<CarDto> getAllCarsWithPages(int PageNo);
    public String deleteCar(int id);


//    public Optional<List<Car>> FindByArea(String area);

    public List<CarDto> searchByFilter(FilterDto filterDto, int pageNo);

    public CarDto findById(int carId);
    List<CarDto> getCarsByDealerIdWithStatus(int dealerId, String status,int pageNo);
}
