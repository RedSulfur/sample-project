package com.spring.german.service.interfaces;

import com.spring.german.entity.UserProfile;

import java.util.List;

public interface UserProfileService {
    List<UserProfile> findAll();
    UserProfile findByType(String type);
}
