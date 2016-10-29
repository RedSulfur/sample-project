package com.spring.german.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("customUserDetailsServiceImplementation")
public class UserDetailsServiceAdapter
        implements org.springframework.security.core.userdetails.UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceAdapter.class);

    private UserDetailsService userDetailsService;

    @Autowired
    public UserDetailsServiceAdapter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDetailsService.getEntityByKey(username);
    }
}