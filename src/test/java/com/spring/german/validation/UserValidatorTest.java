package com.spring.german.validation;

import com.spring.german.entity.User;
import com.spring.german.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserValidatorTest {

    @Mock
    private UserRepository userRepository;

    @Before
    public void setUp() {
        when(userRepository.findBySsoId("validSsoId")).thenReturn(new User());
        given(userRepository.findBySsoId("validSsoId")).willReturn(new User());
        given(userRepository.findBySsoId(anyString())).willReturn(new User());
    }

    @Test
    public void validate() throws Exception {

        UserValidator userValidator = new UserValidator();

        User userToValidate = new User();

        userToValidate.setSsoId("validSsoId");
        userToValidate.setPassword("validPassword");
        userToValidate.setFirstName("validFistName");
        userToValidate.setLastName("validLastName");
        userToValidate.setEmail("valid@gmail.com");

        Errors errors = new BeanPropertyBindingResult(userToValidate, "validUser");
        userValidator.validate(userToValidate, errors);

        assertFalse(errors.hasErrors());
    }
}