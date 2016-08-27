package com.spring.german.service;

import com.spring.german.entity.User;
import com.spring.german.entity.VerificationToken;
import com.spring.german.repository.UserRepository;
import com.spring.german.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Override
    public User save(User u) {
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        User savedUser = userRepository.save(u);

        return savedUser;
    }

    @Override
    public void updateUser(User user) {
        userRepository.save(user);
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

    private boolean emailExist(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return true;
        }
        return false;
    }

    @Override
    public User getUser(String verificationToken) {
        User user = tokenRepository.findByToken(verificationToken).getUser();
        return user;
    }

    @Override
    public VerificationToken getVerificationToken(String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }

    @Override
    public void createVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
    }
}
