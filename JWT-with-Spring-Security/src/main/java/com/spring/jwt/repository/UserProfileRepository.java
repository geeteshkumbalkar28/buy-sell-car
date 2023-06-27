package com.spring.jwt.repository;

import com.spring.jwt.entity.Userprofile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserProfileRepository extends JpaRepository<Userprofile,Integer> {

    @Modifying
    @Query(value = "DELETE FROM dealer_jwt.userprofile WHERE user_profile_id=:user_id", nativeQuery = true)
    public void DeleteById(int user_id);
}