package com.spring.jwt.repository;

import com.spring.jwt.entity.ProfilePhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfilePhotoRepo extends JpaRepository<ProfilePhoto, Long> {
}
