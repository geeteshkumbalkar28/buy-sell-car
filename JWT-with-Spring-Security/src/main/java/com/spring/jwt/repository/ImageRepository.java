package com.spring.jwt.repository;

import com.spring.jwt.entity.CarPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<CarPhoto,Integer> {
}
