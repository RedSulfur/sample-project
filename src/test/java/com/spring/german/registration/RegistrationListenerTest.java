package com.spring.german.registration;

import com.icegreen.greenmail.junit.GreenMailRule;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.spring.german.entity.User;
import com.spring.german.service.interfaces.VerificationTokenService;
import com.spring.german.util.Email;
import com.spring.german.util.EmailUtil;
import com.spring.german.util.EventHandler;
import com.spring.german.util.TestUtil;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.Locale;

import static com.icegreen.greenmail.util.GreenMailUtil.sendTextEmail;
import static com.spring.german.util.TestUtil.getValidEmail;
import static com.spring.german.util.TestUtil.getValidUser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationListenerTest {

    private RegistrationListener registrationListener;
    private OnRegistrationCompleteEvent onRegistrationCompleteEvent;

    @Rule public final GreenMailRule greenMail = new GreenMailRule(ServerSetupTest.SMTP);

    @Mock private VerificationTokenService tokenService;
    @Mock private EmailUtil emailUtil;
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

    @Test
    public void shouldPerformAnEmailDispatch() {
        given(eventHandler.constructEmailForUser(anyString(), any(User.class)))
                .willReturn(getValidEmail());
        ArgumentCaptor<Email> capturedEmail = ArgumentCaptor.forClass(Email.class);
        registrationListener.onApplicationEvent(onRegistrationCompleteEvent);
        verify(emailUtil).sendEmail(capturedEmail.capture());

        this.sendTestMail(capturedEmail);
        assertEquals("some body", GreenMailUtil.getBody(greenMail.getReceivedMessages()[0]));
    }

    private void sendTestMail(ArgumentCaptor<Email> capturedEmail) {

        Email email = capturedEmail.getValue();

        String recipientAddress = email.getRecipientAddress();
        String subject = email.getSubject();
        String body = email.getBody();

        sendTextEmail(recipientAddress, "noreply@gmail.com", subject, body, ServerSetup.SMTP);
    }
}
