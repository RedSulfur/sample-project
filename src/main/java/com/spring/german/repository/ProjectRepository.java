package com.spring.german.repository;

import com.spring.german.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
//    @Query(value = "select * from project where technologies @> CAST('{Spring Boot}' AS varchar[])", nativeQuery = true)
//    List<Project> getProjectsWithSpecificTechnologies();

    List<Project> findByTechnologiesContaining(String key);
}