package com.spring.jwt.repository;

import com.spring.jwt.entity.DocumentPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepo extends JpaRepository<DocumentPhoto,Long> {
}
