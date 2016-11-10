package com.spring.german.registration;

import com.spring.german.entity.User;
import org.springframework.context.ApplicationEvent;

public class OnRegistrationCompleteEvent extends ApplicationEvent {
    private final User user;
    private final ApplicationDetails details;

    public OnRegistrationCompleteEvent(User user, ApplicationDetails details) {
        super(user);
        this.user = user;
        this.details = details;
    }

    public ApplicationDetails getDetails() {
        return details;
    }

    public User getUser() {
        return user;
    }
}
