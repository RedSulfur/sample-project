package com.spring.german.service;

import com.spring.german.entity.Project;
import com.spring.german.repository.ProjectRepository;
import com.spring.german.service.interfaces.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Arrays.asList;

@Service
public class DefaultProjectService implements ProjectService {

    private ProjectRepository projectRepository;

    @Autowired
    public DefaultProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public List<Project> getProjectsByTechnologyNames(String technologies) {

        List<String> technologiesToSearchBy = asList(technologies.split(","));

        List<Project> projects =
                projectRepository.findDistinctByTechnologiesNameIn(technologiesToSearchBy);

        return projects;
    }

    @Override
    public Project save(Project project) {
        return projectRepository.save(project);
    }
}