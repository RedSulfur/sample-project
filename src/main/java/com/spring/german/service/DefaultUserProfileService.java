package com.spring.german.service;

import com.spring.german.entity.UserProfile;
import com.spring.german.repository.UserProfileRepository;
import com.spring.german.service.interfaces.Distinguishing;
import com.spring.german.service.interfaces.Searching;
import com.spring.german.service.interfaces.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userProfileService")
public class DefaultUserProfileService implements UserProfileService,
        Distinguishing<UserProfile>, Searching<UserProfile> {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Override
    public List<UserProfile> findAll() {
        return userProfileRepository.findAll();
    }

    @Override
    public UserProfile findByType(String type) {
        return userProfileRepository.findByType(type);
    }

    @Override
    public UserProfile getEntityByKey(String key) {
        return userProfileRepository.findByType(key);
    }

    @Override
    public UserProfile getById(long id) {
        return userProfileRepository.findOne(id);
    }
}