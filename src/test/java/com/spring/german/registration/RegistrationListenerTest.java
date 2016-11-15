package com.spring.german.registration;

import com.spring.german.entity.User;
import com.spring.german.service.interfaces.VerificationTokenService;
import com.spring.german.util.EmailUtil;
import com.spring.german.util.EventHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Locale;

import static com.spring.german.util.TestUtil.getValidUser;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationListenerTest {

    private RegistrationListener registrationListener;
    private OnRegistrationCompleteEvent onRegistrationCompleteEvent;

    @Mock private EmailUtil emailUtil;
    @Mock private VerificationTokenService tokenService;
    @Mock private EventHandler eventHandler;

    @Before
    public void setUp() throws Exception {
        ApplicationDetails details = new ApplicationDetails("localhost:8080", Locale.ENGLISH);
        onRegistrationCompleteEvent = new OnRegistrationCompleteEvent(getValidUser(), details);
        registrationListener = new RegistrationListener(tokenService, emailUtil, eventHandler);
    }

    @Test
    public void shouldSaveVerificationToken() {
        registrationListener.onApplicationEvent(onRegistrationCompleteEvent);

        verify(tokenService, times(1)).createVerificationToken(any(User.class), anyString());
    }

    @Test
    public void shouldConstructEmailBody() {
        registrationListener.onApplicationEvent(onRegistrationCompleteEvent);

        verify(eventHandler, times(1))
                .getEmailBody(any(OnRegistrationCompleteEvent.class), anyString());
    }

    @Test
    public void shouldConstructEmailObject() {
        registrationListener.onApplicationEvent(onRegistrationCompleteEvent);

        verify(eventHandler, times(1))
                .getEmailBody(any(OnRegistrationCompleteEvent.class), anyString());
    }
}
