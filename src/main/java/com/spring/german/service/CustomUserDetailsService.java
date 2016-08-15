package com.spring.german.service;

import com.spring.german.entity.User;
import com.spring.german.entity.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserService userService;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String ssoId) throws UsernameNotFoundException {

        User user = userService.findBySso(ssoId);
        logger.info("SsoId that is being used for the role fetching: {}", ssoId);
        logger.info("User fetched using ssoId: {}", user);


        if (user == null) {
            System.out.println("User was not found");
            throw new UsernameNotFoundException("User not found");
        }
        /**
         * Constructor parameters for the User class
         *
         * username - the username presented to the DaoAuthenticationProvider
         * password - the password that should be presented to the DaoAuthenticationProvider
         * enabled - set to true if the user is enabled
         * accountNonExpired - set to true if the account has not expired
         * credentialsNonExpired - set to true if the credentials have not expired
         * accountNonLocked - set to true if the account is not locked
         * authorities - the authorities that should be granted to the caller
         * if they presented the correct username and password and the user is enabled. Not null.
         */
        org.springframework.security.core.userdetails.User result = new org.springframework.security.core.userdetails.User(
                user.getSsoId(), user.getPassword(), user.getState().equals("Active")
                , true, true, true, getGrantedAuthorities(user));

        logger.info("Userdetail was created: {}", result);
        return result;
    }

    private List<GrantedAuthority> getGrantedAuthorities(User user){
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        for(UserProfile userProfile : user.getUserProfiles()){
            logger.info("UserProfile: {}", userProfile);
            logger.info("**********WARN*********** ROLE_ + ", userProfile.getType());
            authorities.add(new SimpleGrantedAuthority("ROLE_" + userProfile.getType()));
        }
        logger.info("authorities: {}", authorities);

        return authorities;
    }

}
