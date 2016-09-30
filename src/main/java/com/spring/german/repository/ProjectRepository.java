package com.spring.german.repository;

import com.spring.german.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findDistinctByTechnologiesNameIn(List<String> names);
}