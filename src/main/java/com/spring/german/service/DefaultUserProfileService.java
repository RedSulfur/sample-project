package com.spring.german.service;

import com.spring.german.entity.UserProfile;
import com.spring.german.repository.UserProfileRepository;
import com.spring.german.service.interfaces.Distinguishing;
import com.spring.german.service.interfaces.Searching;
import com.spring.german.service.interfaces.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    //TODO: implement appropriate exception
    @Override
    public UserProfile getEntityByKey(String key) {
        return Optional.of(userProfileRepository.findByType(key))
                .orElseThrow(() -> new RuntimeException("mock"));
    }

    @Override
    public UserProfile getById(long id) {
        return userProfileRepository.findOne(id);
    }
}