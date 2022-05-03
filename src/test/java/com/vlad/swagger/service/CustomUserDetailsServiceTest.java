package com.vlad.swagger.service;

import com.vlad.swagger.entity.Person;
import com.vlad.swagger.repository.PersonRepository;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

@SpringBootTest
public class CustomUserDetailsServiceTest {

    @MockBean
    private PersonRepository personRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @BeforeEach
    public void setUp() {

        Person user = new Person();
        user.setId(1);
        user.setName("User01");
        user.setPassword("12345");

        Mockito.when(personRepository.
                findByName(user.getName())).thenReturn(user);
    }

    @Test
    public void getloadUserByUsernameSuccessTest() {

        String username = "User01";

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        assertThat(customUserDetailsService.loadUserByUsername(username)).isEqualTo(userDetails);
    }

    @Test
    public void getloadUserByUsernameFailTest() {

        String username = "User02";

        assertThrows(
                UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername(username));
    }

}
