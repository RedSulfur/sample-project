package com.spring.german.service;

import com.spring.german.entity.Project;
import com.spring.german.entity.Technology;
import com.spring.german.entity.User;
import com.spring.german.exceptions.TechnologiesNotFoundException;
import com.spring.german.repository.ProjectRepository;
import com.spring.german.service.interfaces.ProjectService;
import com.spring.german.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;

@Service
public class DefaultProjectService implements ProjectService {

    private ProjectRepository projectRepository;
    private UserService userService;

    @Autowired
    public DefaultProjectService(ProjectRepository projectRepository,
                                 UserService userService) {
        this.projectRepository = projectRepository;
        this.userService = userService;
    }

    @Override
    public List<Project> getProjectsByTechnologyNames(String technologies) {
        ofNullable(technologies)
                .orElseThrow(() -> new TechnologiesNotFoundException("You specified no technologies to search by"));
        List<String> technologiesToSearchBy = asList(technologies.split(","));

        return projectRepository
                .findDistinctByTechnologiesNameIn(technologiesToSearchBy);
    }

    @Override
    public Project save(Project project) {
        return projectRepository.save(project);
    }

    /**
     * Maps a list of the received strings that represent technologies
     * to the list of the corresponding objects.
     * These {@link com.spring.german.entity.Technology} objects are being
     * associated with the extracted user and his new project.
     *
     * @param username     string that is used to extract from the database
     *                     an object that represents currently logged in user.
     *                     {@see User}
     * @param technologies list of strings that is used to create a list of
     *                     {@link com.spring.german.entity.Technology} objects
     *                     for the further association
     */
    public void saveProjectWithTechnologies(String username, List<String> technologies) {

        User user = userService.getUserBySsoId(username);

        Project project = new Project("default", user);                                                     //TODO: Project name should be defined by user, of course. This feature just isn't ready yet.
        List<Technology> technologiesToSave = technologies.stream()
                .map(t -> new Technology(t, project)).collect(Collectors.toList());
        project.setTechnologies(technologiesToSave);

        projectRepository.save(project);
    }
}