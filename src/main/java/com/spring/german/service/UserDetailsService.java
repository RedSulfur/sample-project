package com.spring.german.service;

import com.spring.german.entity.User;
import com.spring.german.entity.UserProfile;
import com.spring.german.service.interfaces.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsService {

    private final static Logger log = LoggerFactory.getLogger(UserDetailsService.class);

    private UserService userService;

    @Autowired
    public UserDetailsService(UserService userService) {
        this.userService = userService;
    }

    public UserDetails getEntityByKey(String key) {

        User user = userService.getUserBySsoId(key);

        if(user == null) {
            throw new UsernameNotFoundException("Username not found");
        }

        org.springframework.security.core.userdetails.User result = new org.springframework.security.core.userdetails.User( //TODO: Rename User entity to AppUser?
                user.getSsoId(), user.getPassword(), user.getState().equals("Active")
                , true, true, true, getGrantedAuthorities(user));

        log.info("UserDetail was created: {}", result);

        return result;
    }

    private List<GrantedAuthority> getGrantedAuthorities(User user){
        List<GrantedAuthority> authorities = new ArrayList<>();

        for(UserProfile userProfile : user.getUserProfiles()){
            log.info("Profiles for the given user: {}", userProfile);
            log.info("ROLE_ + ", userProfile.getType());
            authorities.add(new SimpleGrantedAuthority("ROLE_" + userProfile.getType()));
        }
        log.info("authorities: {}", authorities);

        return authorities;
    }
}