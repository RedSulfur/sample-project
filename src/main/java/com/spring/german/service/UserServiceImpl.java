package com.spring.german.service;

import com.spring.german.entity.User;
import com.spring.german.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public User save(User u) {
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        User savedUser = userRepository.save(u);

        return savedUser;
    }

    @Override
    public User findById(long id) {
        User user = userRepository.findOne(id);
        return user;
    }

    @Override
    public User findBySso(String sso) {
        User user = userRepository.findBySsoId(sso);
        return user;
    }
}
