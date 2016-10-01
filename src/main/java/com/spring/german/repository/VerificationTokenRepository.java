package com.spring.german.repository;

import com.spring.german.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository
        extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);
}
