package com.spring.german.service;

import com.spring.german.entity.Project;
import com.spring.german.entity.State;
import com.spring.german.entity.Technology;
import com.spring.german.entity.User;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class ProjectTestUtil {

    public static final String VALID_TOKEN = "valid-token";
    public static final String VALID_SSO_ID = "valid-sso";
    public static final String VALID_EMAIL = "valid-email";
    public static final long EXISTING_ID = 1L;

    public List<Project> getListsOfProjects() {

        List<Technology> technologies = Stream.of("Travis Build", "Maven", "Maven", "Spring validation",
                "Gradle", "Maven", "Maven")
                .map(Technology::new).collect(toList());

        return this.splitInChunks(technologies, 2).stream()
                .map(l -> new Project("test-logo", l, new User()))
                .collect(Collectors.toList());
    }

    private <T> List<List<T>> splitInChunks(List<T> listToSplit, int length) {
        int listSize = listToSplit.size();
        int fullChunks = (listSize - 1) / length;
        return IntStream.range(0, fullChunks + 1)
                .mapToObj(n -> listToSplit.subList(n * length, n == fullChunks ? listSize : (n + 1) * length))
                .collect(toList());
    }

    public List<String> getValidTechnologies() {
        return Stream.of(("Travis Build,Maven,Spring validation").split(","))
                .collect(Collectors.toList());
    }

    public Project getValidProject() {
        return new Project("test-logo", new User());
    }

    public User getValidUser() {
        return new User("test-sso", "test-password", "test-email", State.INACTIVE.getState(), new HashSet<>());
    }
}
