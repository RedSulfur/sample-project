package com.spring.german.config;

import com.spring.german.entity.UserProfile;
import com.spring.german.service.CustomUserDetailsService;
import com.spring.german.service.UserProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 *
 * public interface Converter<S,T>
 *
 * A converter converts a source object of type S to a target of type T.
 */
@Component("roleToUserProfileConverter")
public class RoleToUserProfileConverter implements Converter<Object, UserProfile> {

    Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    UserProfileService userProfileService;

    /*
     * Gets UserProfile by Id
     * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
     */
    public UserProfile convert(Object element) {
        Integer id = Integer.parseInt((String)element);
        UserProfile profile= userProfileService.findById(id);
        logger.info("Profile: {}", profile);
        return profile;
    }
}
