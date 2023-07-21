package com.spring.jwt.Interfaces;

import com.spring.jwt.dto.CarDto;
import com.spring.jwt.dto.FilterDto;

import java.util.List;

public interface FilterService {
    public List<CarDto> searchByFilter(FilterDto filterDto, int pageNo);
    public List<CarDto> getAllCarsWithPages(int PageNo);

}
