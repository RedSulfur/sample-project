package com.spring.german.service.interfaces;

import com.spring.german.entity.Project;

import java.util.List;

public interface ProjectService {
    List<Project> findByTechnologyNames(String technologies);
}
