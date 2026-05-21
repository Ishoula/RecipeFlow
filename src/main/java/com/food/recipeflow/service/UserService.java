package com.food.recipeflow.service;

import com.food.recipeflow.entity.User;
import com.food.recipeflow.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository=userRepository;
    }
    public User saveUser(User user){
        return userRepository.save(user);
    }
    public Optional<User> findByUsername(String username){
        return userRepository.findByUsername(username);
    }
    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }
}
