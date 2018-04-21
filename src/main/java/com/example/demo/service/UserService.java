package com.example.demo.service;

import java.util.Optional;

import com.example.demo.model.User;

public interface UserService {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    User saveUser(User user);

}

