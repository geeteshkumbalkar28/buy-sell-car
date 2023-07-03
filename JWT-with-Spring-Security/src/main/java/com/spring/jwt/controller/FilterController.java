package com.spring.jwt.controller;

import com.spring.jwt.dto.CarDto;
import com.spring.jwt.dto.FilterDto;
import com.spring.jwt.service.FilterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cars")
public class FilterController {

    private final FilterService filterService;

    @GetMapping("/mainFilter/{pageNo}")
    public List<CarDto> searchByFilter(@RequestBody FilterDto filterDto, @PathVariable int pageNo){
        return filterService.searchByFilter(filterDto, pageNo);

    }
}
