package com.glebzapara.nexor.services;

import com.glebzapara.nexor.models.User;
import com.glebzapara.nexor.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
