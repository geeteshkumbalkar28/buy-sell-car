package com.spring.jwt.repository;

import com.spring.jwt.dto.DealerDto;
import com.spring.jwt.entity.Dealer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DealerRepository extends JpaRepository<Dealer, Integer> {
    void deleteById(int dealerId);
}
