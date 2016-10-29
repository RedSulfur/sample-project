package com.spring.german.repository;

import com.spring.german.entity.Project;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProjectRepositoryTest {

    public static final List<String> TECHNOLOGIES_TO_SEARCH_BY = Arrays.asList("Ant", "Gradle");

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    public void shouldFindAllTheProjectsWhoseTechnologiesAreContainedInList()
            throws Exception {

        List<Project> projects = projectRepository.findDistinctByTechnologiesNameIn(TECHNOLOGIES_TO_SEARCH_BY);

        assertThat(projects.size(), Matchers.is(5));
    }
}