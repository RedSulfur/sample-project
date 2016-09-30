package com.spring.german.repository;

import com.spring.german.entity.Technology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TechnologyRepository extends JpaRepository<Technology, Long> {
    List<Technology> findTechnologiesByProjectId(Long id);
}
