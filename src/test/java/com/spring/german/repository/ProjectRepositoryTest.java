package com.spring.german.repository;

import com.spring.german.entity.Project;
import com.spring.german.entity.Technology;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProjectRepositoryTest {

    public static final List<String> TECHNOLOGIES_TO_SEARCH_BY = Arrays.asList("Git", "Gradle");

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    public void shouldFindAllTheProjectsThatCorrespondToTheInput() {

        List<Project> projectsByTechnologyNames = projectRepository
                .findDistinctByTechnologiesNameIn(TECHNOLOGIES_TO_SEARCH_BY);

        List<String> technologies = projectsByTechnologyNames.stream()
                .map(Project::getTechnologies)
                .flatMap(List::stream)
                .map(Technology::getName)
                .collect(Collectors.toList());

        assertThat(projectsByTechnologyNames.size(), Matchers.is(4));
        assertThat(technologies, Matchers.hasItems("Git", "Gradle"));
    }
}