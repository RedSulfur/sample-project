package com.spring.german.repository;

import com.spring.german.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.ssoId = :ssoId")
    User findBySsoId(@Param("ssoId") String ssoId);

    User findByEmail(String email);
}
