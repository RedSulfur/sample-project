package com.spring.german.service;

import com.spring.german.entity.UserProfile;
import com.spring.german.exceptions.TokenNotFoundException;
import com.spring.german.repository.UserProfileRepository;
import com.spring.german.service.interfaces.Distinguishing;
import com.spring.german.service.interfaces.Searching;
import com.spring.german.service.interfaces.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Optional.ofNullable;

@Service("userProfileService")
public class DefaultUserProfileService implements UserProfileService,
        Distinguishing<UserProfile> {

    private UserProfileRepository userProfileRepository;

    @Autowired
    public DefaultUserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public List<UserProfile> findAll() {
        return userProfileRepository.findAll();
    }

    @Override
    public UserProfile findByType(String type) {
        return ofNullable(userProfileRepository.findByType(type))
                .orElseThrow(() -> new TokenNotFoundException("Requested token is not present in database"));
    }

    @Override
    public UserProfile getById(long id) {
        return userProfileRepository.findOne(id);
    }
}