package com.spring.german.service;

import com.spring.german.entity.Project;
import com.spring.german.entity.Technology;
import com.spring.german.entity.User;
import com.spring.german.exceptions.EmptyRepositoryNameException;
import com.spring.german.repository.ProjectRepository;
import com.spring.german.service.interfaces.Searching;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import static java.util.Optional.of;
import static java.util.regex.Pattern.compile;

@Service
public class CollaborationService {

    private Searching<User> userService;
    private ProjectRepository projectRepository;

    @Autowired
    public CollaborationService(Searching<User> userService,
                                ProjectRepository projectRepository) {
        this.userService = userService;
        this.projectRepository = projectRepository;
    }

    private static final String REGEX = "\\[([a-zA-z ]*)\\]\\(.+\\)";

    //TODO: Rework javadoc
    /**
     *
     * and applies a regular expression to it in order to determine the name of every
     * technology that was used to create a project from the given repository.
     *
     * @return          technologies list
     */
    public List<String> getReadmeFromGithubRepositoy(String userName, String repoNameOptional) {

        String body = Project.getReadmeFromGithubRepository(userName, repoNameOptional);

        return extractTechnologyNamesFromReadmeBody(body);
    }

    private List<String> extractTechnologyNamesFromReadmeBody(String body) {

        List<String> technologies = new ArrayList<>();
        Matcher m = compile(REGEX).matcher(body);
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
     *                     {@link User} from the database
     * @param technologies list of strings that is used to create a list of
     *                     {@link com.spring.german.entity.Technology} objects
     *                     for the further association
     */
    public void saveProjectWithTechnologies(String username, List<String> technologies) {

        User user = userService.getEntityByKey(username);

        Project project = new Project("default", user);
        List<Technology> technologiesToSave = technologies.stream()
                .map(t -> new Technology(t, project)).collect(Collectors.toList());
        project.setTechnologies(technologiesToSave);

        projectRepository.save(project);
    }


    public void populateSessionWithTechnologies(String userName,
                                                 String repoName,
                                                 HttpServletRequest request) {

        String notEmptyRepoName = of(repoName).orElseThrow(() ->
                new EmptyRepositoryNameException("You have not provided any repository name"));
        List<String> technologies =
                getReadmeFromGithubRepositoy(userName, notEmptyRepoName);
        request.getSession().setAttribute("technologies", technologies);
    }
}