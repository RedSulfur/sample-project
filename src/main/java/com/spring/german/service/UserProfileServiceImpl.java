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
        return userProfileRepository.findAll();
    }

    @Override
    public UserProfile findByType(String type) {
        return userProfileRepository.findByType(type);
    }

    @Override
    public UserProfile findById(long id) {
        return userProfileRepository.findOne(id);
    }
}
