package com.spring.jwt.service.impl;

import com.spring.jwt.dto.CarDto;
import com.spring.jwt.dto.FilterDto;
import com.spring.jwt.entity.Car;
import com.spring.jwt.repository.CarRepo;
import com.spring.jwt.service.FilterService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

        Pageable pageable = PageRequest.of(pageNo - 1, 5);

        Page<Car> carPage = carRepo.findAll(spec, pageable);

        List<CarDto> listOfCarDto = carPage.getContent().stream()
                .map(CarDto::new)
                .collect(Collectors.toList());

        return listOfCarDto;
    }
}
