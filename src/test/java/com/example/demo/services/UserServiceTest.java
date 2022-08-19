package com.example.demo.services;

import com.example.demo.DemoApplication;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DemoApplication.class)
class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
    }


    @Test
    public void canGetAllUsers() {
        //when
        userService.findAll();
        //then
        verify(userRepository).findAll();

    }

    @Test
    void canSaveUser() {
        User givenUser = new User(12L, "jacops"
                , "handlers", "password",
                "jacop@email.com", new HashSet<>());

        //when
        userService.save(givenUser);
        //then
        verify(userRepository).save(givenUser);

        //test for exception handling when user is null
        userService.save(null);
    }

    @Test
    void findByEmail() {
        String email = "jacop@handler.com";
        when(userRepository.findByEmail(email)).thenReturn(new User());
        //when
        userService.findByEmail(email);
        //then
        verify(userRepository).findByEmail(email);
    }

}