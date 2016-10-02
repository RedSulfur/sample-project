package com.spring.german.service;

import com.spring.german.entity.Project;
import com.spring.german.entity.Technology;
import com.spring.german.entity.User;
import com.spring.german.repository.ProjectRepository;
import com.spring.german.util.ReadmeParser;
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
    private ReadmeParser readmeParser;

    @Autowired
    public CollaborationService(UserService userService,
                                ProjectRepository projectRepository,
                                ReadmeParser readmeParser) {
        this.userService = userService;
        this.projectRepository = projectRepository;
        this.readmeParser = readmeParser;
    }

    private static final String REGEX = "\\[([a-zA-z ]*)\\]\\(.+\\)";

    /**
     * Takes a string that was returned by {@code ReadmeParser#parseReadmeFromGithubRepository}
     * and applies a regular expression to it in order to determine the name of every
     * technology that was used to create a project from the given repository.
     *
     * @param username  user's github nickname
     * @param repoName  user's repository name
     * @return          technologies list
     */
    public List<String> getTechnologiesFromGithubRepositoy(String username, String repoName) {

        String body = readmeParser.parseReadmeFromGithubRepository(username, repoName);

        List<String> technologies = new ArrayList<>();
        Matcher m = Pattern.compile(REGEX).matcher(body);
        while (m.find()) {
            technologies.add(m.group(1));
        }
        return technologies;
    }

    /**
     * Maps a list of the obtained strings that represent technologies
     * to the list of the corresponding objects.
     * These {@link com.spring.german.entity.Technology} objects are being
     * associated with the extracted user and a new project. This entwined
     * chain is being saved afterwards.
     *
     * @param username     string that is used to extract a corresponding
     *                     {@link User} object from the database
     * @param technologies list of strings that is used to create a list of
     *                     {@link com.spring.german.entity.Technology} objects
     *                     for the further association
     */
    public void saveProjectWithTechnologies(String username, List<String> technologies) {

        User user = userService.findBySso(username);

        Project project = new Project("default", user);

        List<Technology> technologiesToSave = technologies.stream()
                .map(t -> new Technology(t, project)).collect(Collectors.toList());

        project.setTechnologies(technologiesToSave);

        projectRepository.save(project);
    }
}