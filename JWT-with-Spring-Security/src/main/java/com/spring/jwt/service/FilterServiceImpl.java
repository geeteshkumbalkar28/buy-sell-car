package com.spring.jwt.service;

import com.spring.jwt.dto.CarDto;
import com.spring.jwt.dto.FilterDto;
import com.spring.jwt.entity.Car;
import com.spring.jwt.exception.CarNotFoundException;
import com.spring.jwt.exception.PageNotFoundException;
import com.spring.jwt.repository.CarRepo;
import com.spring.jwt.Interfaces.FilterService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FilterServiceImpl implements FilterService {

    @Autowired
    private CarRepo carRepo;

    @Override
    public List<CarDto> searchByFilter(FilterDto filterDto, int pageNo) {
        Specification<Car> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filterDto.getMinPrice() != null) {
                predicates.add(criteriaBuilder.greaterThan(root.get("price"), filterDto.getMinPrice()));
            }
            if (filterDto.getMaxPrice() != null) {
                predicates.add(criteriaBuilder.lessThan(root.get("price"), filterDto.getMaxPrice()));
            }

            if (filterDto.getArea() != null && !filterDto.getArea().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("area"), filterDto.getArea()));
            }
            if (filterDto.getYear() != 0) {
                predicates.add(criteriaBuilder.equal(root.get("year"), filterDto.getYear()));
            }
            if (filterDto.getBrand() != null && !filterDto.getBrand().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("brand"), filterDto.getBrand()));
            }
            if (filterDto.getModel() != null && !filterDto.getModel().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("model"), filterDto.getModel()));
            }
            if (filterDto.getTransmission() != null && !filterDto.getTransmission().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("transmission"), filterDto.getTransmission()));
            }
            if (filterDto.getFuelType() != null && !filterDto.getFuelType().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("fuelType"), filterDto.getFuelType()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
//        spec

        Pageable pageable = PageRequest.of(pageNo  -0, 10);

        Page<Car> carPage = carRepo.findAll(spec, pageable);
        if(carPage.isEmpty()){
            throw new PageNotFoundException("Page Not found");
        }
        List<CarDto> listOfCarDto =new ArrayList<>();

        for (int counter=0;counter<carPage.getContent().size();counter++){

            CarDto carDto = new CarDto(carPage.getContent().get(counter));
            carDto.setCarId(carPage.getContent().get(counter).getId());
            listOfCarDto.add(carDto);
        }

        return listOfCarDto;
    }
    @Override
    public List<CarDto> getAllCarsWithPages(int PageNo) {
        List<Car> listOfCar = carRepo.findAll();
        CarNotFoundException carNotFoundException;
        if((PageNo*10)>listOfCar.size()-1){
            throw new PageNotFoundException("page not found");

        }
        if(listOfCar.size()<=0){throw new CarNotFoundException("car not found",HttpStatus.NOT_FOUND);}
//        System.out.println("list of de"+listOfCar.size());
        List<CarDto> listOfCarDto = new ArrayList<>();

        int pageStart=PageNo*10;
        int pageEnd=pageStart+10;
        int diff=(listOfCar.size()) - pageStart;
        for(int counter=pageStart,i=1;counter<pageEnd;counter++,i++){
            if(pageStart>listOfCar.size()){break;}

//            System.out.println("*");
            CarDto carDto = new CarDto(listOfCar.get(counter));
            carDto.setCarId(listOfCar.get(counter).getId());
            listOfCarDto.add(carDto);
            if(diff == i){
                break;
            }
        }

//        System.out.println(listOfCar);
        return listOfCarDto;
    }


}
