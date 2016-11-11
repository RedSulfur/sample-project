package com.spring.german.service;

import com.spring.german.entity.Project;
import com.spring.german.entity.Technology;
import com.spring.german.entity.User;
import com.spring.german.exceptions.ReadmeNotFoundException;
import com.spring.german.repository.ProjectRepository;
import com.spring.german.service.interfaces.UserService;
import com.spring.german.util.GitHubRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import static java.util.regex.Pattern.compile;

@Service
public class CollaborationService {

    private ProjectRepository projectRepository;
    private UserService userService;

    // e.g. "[Spring Thymeleaf](http://www.thymeleaf.org/doc/tutorials/2.1/thymeleafspring.html)"
    // matches "Spring Thymeleaf"
    private static final String TECHNOLOGY_NAME_BY_GITHUB_README_REFERENCE = "\\[([a-zA-z ]*)\\]\\(.+\\)"; //TODO: Is it a correct place for this variable? Isn't the name too long?

    @Autowired
    public CollaborationService(ProjectRepository projectRepository,
                                UserService userService) {
        this.userService = userService;
        this.projectRepository = projectRepository;
    }

    public void populateSessionWithTechnologiesFromRepo(HttpSession session,
                                                        GitHubRepository gitHubRepository) {

        String readmeBody = this.getReadmeFromGitHubRepository(gitHubRepository);
        List<String> technologies = this.extractTechnologyNamesFromReadmeBody(readmeBody);
        session.setAttribute("technologies", technologies);
    }

    private String getReadmeFromGitHubRepository(GitHubRepository gitHubRepository) {

        String readmeBody;
        try {
            URL url = new URL("https://raw.githubusercontent.com/"
                    + gitHubRepository.getOwnerName() + "/"
                    + gitHubRepository.getRepoName() + "/master/README.md");
            URLConnection con = url.openConnection();
            InputStream in = con.getInputStream();
            String encoding = con.getContentEncoding();
            encoding = encoding == null ? "UTF-8" : encoding;
            readmeBody = IOUtils.toString(in, encoding);
        } catch (IOException e) {
            throw new ReadmeNotFoundException("There is no such user on github, or " +
                    "repository name you've specified is non existent", e);
        }

        return readmeBody;
    }

    private List<String> extractTechnologyNamesFromReadmeBody(String body) {

        List<String> technologies = new ArrayList<>();
        Matcher m = compile(TECHNOLOGY_NAME_BY_GITHUB_README_REFERENCE).matcher(body);
        while (m.find()) {
            technologies.add(m.group(1));
        }

        return technologies;
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

        Project project = new Project("default", user);                                                     //TODO: Project name should be defined by user, of course. It is just a matter of time.
        List<Technology> technologiesToSave = technologies.stream()
                .map(t -> new Technology(t, project)).collect(Collectors.toList());
        project.setTechnologies(technologiesToSave);

        projectRepository.save(project);
    }
}