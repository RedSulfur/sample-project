package com.spring.german.repository;

import com.spring.german.entity.Technology;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TechnologyRepositoryTest {

    @Autowired
    private TechnologyRepository technologyRepository;

    private List<Technology> technologiesToSave;

    @Before
    public void setUp() {
        technologiesToSave = Arrays.stream("Travis Build,Spring Thymeleaf,Spring MVC,Spring validation,Gradle,Spring Security,Bootstrap"
                .split(",")).map(Technology::new).collect(Collectors.toList());
    }

    @Test
    public void shouldSaveAllTechnologiesFromList()
            throws Exception {

        List<Technology> savedTechnologies = technologyRepository.save(technologiesToSave);
        assertThat(savedTechnologies, hasSize(7));
    }

    @Test
    public void shouldReturnUpdatedTechnologyAfterUpdate()
            throws Exception {

        Technology sampleTechnology = technologyRepository.findOne(1L);
        assertThat(sampleTechnology.getName(), is("Spring Data"));

        sampleTechnology.setName("Spring Batch");

        Technology updatedTechnology = technologyRepository.save(sampleTechnology);
        assertThat(updatedTechnology.getName(), is("Spring Batch"));
    }
}