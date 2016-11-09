package com.spring.german.service.interfaces;

/*
T getEntityByKey(String key);
is not called T searchEntityByKey for
consistency purposes: "Pick one word per concept"
and i already have
List<String> getReadmeFromGithubRepositoy
List<Project> getProjectsByTechnologyNames
List<GrantedAuthority> getGrantedAuthorities
and so on so forth
 */
public interface Searching<T> {
    T getEntityByKey(String key);
}