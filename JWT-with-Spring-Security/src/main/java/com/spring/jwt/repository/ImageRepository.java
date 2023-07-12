package com.spring.jwt.repository;

import com.spring.jwt.entity.Carphoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Carphoto,Integer> {
}
