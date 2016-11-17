package com.spring.german.service;

import com.spring.german.exceptions.RepositoryNotSpecifiedException;
import com.spring.german.util.GitHubRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;

import static java.util.regex.Pattern.compile;

@Service
public class CollaborationService {

    // e.g. "[Spring Thymeleaf](http://www.thymeleaf.org/doc/tutorials/2.1/thymeleafspring.html)"
    // matches "Spring Thymeleaf"
    private static final String TECHNOLOGY_NAME_BY_GITHUB_README_REFERENCE = "\\[([a-zA-z ]*)\\]\\(.+\\)"; //TODO: Is it a correct place for this variable? Isn't the name too long?

    public void populateSessionWithTechnologiesFromRepo(HttpSession session,
                                                        GitHubRepository gitHubRepository) {
        GitHubRepository notEmptyGitHubRepository = this.getNotEmptyGithubRepository(gitHubRepository);
        String readmeBody = notEmptyGitHubRepository.getReadmeAsString();
        List<String> technologies = this.extractTechnologyNamesFromReadmeBody(readmeBody);
        session.setAttribute("technologies", technologies);
    }

    private GitHubRepository getNotEmptyGithubRepository(GitHubRepository gitHubRepository) {
        return Optional.ofNullable(gitHubRepository)
                .orElseThrow(() -> new RepositoryNotSpecifiedException("You have to specify repository name"));
    }

    private List<String> extractTechnologyNamesFromReadmeBody(String body) {
        List<String> technologies = new ArrayList<>();
        Matcher matcher = compile(TECHNOLOGY_NAME_BY_GITHUB_README_REFERENCE).matcher(body);
        while (matcher.find()) {
            technologies.add(matcher.group(1));
        }

        return technologies;
    }
}