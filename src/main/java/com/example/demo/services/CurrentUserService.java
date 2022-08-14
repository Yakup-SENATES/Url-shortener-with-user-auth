package com.example.demo.services;

import com.example.demo.model.User;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {
    private User user;
    public void setUser(User user) {
        this.user = user;
    }
    public User getUser() {
        return user;
    }
}


