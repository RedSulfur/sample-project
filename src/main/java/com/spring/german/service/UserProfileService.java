package com.spring.german.service;


import com.spring.german.entity.UserProfile;

import java.util.List;

public interface UserProfileService {

    List<UserProfile> findAll();
    UserProfile findByType(String type);
    UserProfile findById(long id);

}
