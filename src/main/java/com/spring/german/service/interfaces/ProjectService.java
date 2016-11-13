package com.spring.german.service.interfaces;

import com.spring.german.entity.Project;

import java.util.List;

public interface ProjectService {

    List<Project> getProjectsByTechnologyNames(String technologies);

    void saveProjectWithTechnologies(String username, List<String> technologies);
    
    Project save(Project project);
}
