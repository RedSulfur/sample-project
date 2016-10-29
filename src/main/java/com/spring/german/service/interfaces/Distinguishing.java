package com.spring.german.service.interfaces;

/*
"Pick one word per concept"
i already have
List<String> getTechnologiesFromGithubRepositoy
List<Project> getProjectsByTechnologyNames
List<GrantedAuthority> getGrantedAuthorities
and so on so forth

searching and finding cannot be sticked together, because
it forces other services to implement methods they don't need

Finding ==> Distinguishing
 */
public interface Distinguishing<T> {
    T getById(long id);
}