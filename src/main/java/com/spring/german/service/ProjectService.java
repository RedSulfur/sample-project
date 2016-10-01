package com.spring.german.service;

import com.spring.german.entity.Project;
import com.spring.german.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ProjectService {

    private ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> findByTechnologyNames(String technologies) {

        List<String> technologiesToSearchBy = Arrays.asList(technologies.split(","));

        List<Project> projects =
                projectRepository.findDistinctByTechnologiesNameIn(technologiesToSearchBy);

        return projects;
    }
}
