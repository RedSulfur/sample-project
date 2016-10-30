package com.spring.german.service;

import com.spring.german.entity.State;
import com.spring.german.entity.User;
import com.spring.german.repository.UserRepository;
import com.spring.german.repository.VerificationTokenRepository;
import com.spring.german.service.interfaces.Distinguishing;
import com.spring.german.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DefaultUserService implements UserService,
        Distinguishing<User> {

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
    public User getUserBySsoId(String ssoId) {
        return userRepository.findBySsoId(ssoId);
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
    public User updateUserState(User user) {
        user.setState(State.ACTIVE.getState());
        return userRepository.save(user);
    }

    @Override
    public User getById(long id) {
        return userRepository.findOne(id);
    }

    private boolean emailExists(String email) {
        User user = userRepository.findByEmail(email);
        return user != null;
    }
}