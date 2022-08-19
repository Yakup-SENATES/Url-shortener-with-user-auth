package com.example.demo.services;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class UserServiceTestConfiguration {

    @Bean
    @Primary
    public UserService userService() {
        return Mockito.mock(UserService.class);
    }


    /*
    * The @Profile annotation tells Spring to apply this configuration only when the “test” profile is active.
    * The @Primary annotation is there to make sure this instance is used instead of a real one for autowiring.
    * The method itself creates and returns a Mockito mock of our UserService class.
    * */
}
