package com.spring.german.repository;

import com.spring.german.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query(value = "select * from project where technologies @> CAST('{Maven, JPA}' AS varchar[])", nativeQuery = true)
    List<Project> getProjectsWithSpecificTechnologies();
}