package com.spring.german.service;

import com.spring.german.entity.UserProfile;
import com.spring.german.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userProfileService")
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Override
    public List<UserProfile> findAll() {
        List<UserProfile> userProfiles = userProfileRepository.findAll();
        return userProfiles;
    }

    @Override
    public UserProfile findByType(String type) {
        UserProfile userProfile = userProfileRepository.findByType(type);
        return userProfile;
    }

    @Override
    public UserProfile findById(long id) {
        UserProfile userProfile = userProfileRepository.findOne(id);
        return userProfile;
    }
}
