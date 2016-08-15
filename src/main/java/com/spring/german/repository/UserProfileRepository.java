package com.spring.german.repository;

import com.spring.german.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    @Query("select p from UserProfile p where p.type = :type")
    UserProfile findByType(@Param("type") String type);
}
