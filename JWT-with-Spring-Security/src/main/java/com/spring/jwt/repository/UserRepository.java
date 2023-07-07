package com.spring.jwt.repository;

import com.spring.jwt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    @Modifying
    @Query(value = "DELETE user_role, userprofile, users FROM user_role " +
            "LEFT JOIN userprofile ON user_role.user_id = userprofile.user_id " +
            "LEFT JOIN users ON user_role.user_id = users.user_id WHERE user_role.user_id = ?1", nativeQuery = true)
    public void DeleteById(int user_id);

    User findByResetPasswordToken(String token);
}
