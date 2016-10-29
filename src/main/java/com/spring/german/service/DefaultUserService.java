package com.spring.german.service;

import com.spring.german.entity.User;
import com.spring.german.repository.UserRepository;
import com.spring.german.repository.VerificationTokenRepository;
import com.spring.german.service.interfaces.Finding;
import com.spring.german.service.interfaces.Searching;
import com.spring.german.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DefaultUserService implements UserService,
        Finding<User>,
        Searching<User> {

    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;

    @Autowired
    public DefaultUserService(PasswordEncoder passwordEncoder,
                              UserRepository userRepository,
                              VerificationTokenRepository tokenRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public User save(User u) {
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        return userRepository.save(u);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findById(long id) {
        return userRepository.findOne(id);
    }

    @Override
    public User searchEntityByKey(String key) {
        return userRepository.findBySsoId(key);
    }

    private boolean emailExists(String email) {
        User user = userRepository.findByEmail(email);
        return user != null;
    }
}