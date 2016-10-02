package com.spring.german.service;

import com.spring.german.entity.Project;
import com.spring.german.entity.Technology;
import com.spring.german.entity.User;
import com.spring.german.repository.ProjectRepository;
import com.spring.german.util.ReadmeReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class CollaborationService {

    private UserService userService;
    private ProjectRepository projectRepository;
    private ReadmeReader readmeReader;

    @Autowired
    public CollaborationService(UserService userService,
                                ProjectRepository projectRepository,
                                ReadmeReader readmeReader) {
        this.userService = userService;
        this.projectRepository = projectRepository;
        this.readmeReader = readmeReader;
    }

    private static final String REGEX = "\\[([a-zA-z ]*)\\]\\(.+\\)";


    public List<String> getTechnologiesFromGithubRepositoy(String username, String repoName) {

        String body = readmeReader.readReadmeFromGithubRepository(username, repoName);

        List<String> technologies = new ArrayList<>();
        Matcher m = Pattern.compile(REGEX).matcher(body);
        while (m.find()) {
            technologies.add(m.group(1));
        }
        return technologies;
    }

    public void saveProjectWithTechnologies(String username, List<String> technologies) {

        User user = userService.findBySso(username);

        Project project = new Project("default", user);

        List<Technology> technologiesToSave = technologies.stream()
                .map(t -> new Technology(t, project)).collect(Collectors.toList());

        project.setTechnologies(technologiesToSave);

        projectRepository.save(project);
    }
}