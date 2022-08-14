package com.example.demo.services;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public void save(User user) {
        userRepository.save(user);
    }

    public User findByEmail(String username) {
        return userRepository.findByEmail(username);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
