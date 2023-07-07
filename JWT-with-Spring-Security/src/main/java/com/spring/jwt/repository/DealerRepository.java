package com.spring.jwt.repository;

import com.spring.jwt.dto.DealerDto;
import com.spring.jwt.entity.Dealer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.expression.spel.ast.OpAnd;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DealerRepository extends JpaRepository<Dealer, Integer> {
    void deleteById(int dealerId);
    public Optional<Dealer> findByEmail(String email);
}
