package com.mermershop.service;

import com.mermershop.dto.LoginRequest;
import com.mermershop.dto.RegisterRequest;
import com.mermershop.model.User;
import com.mermershop.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public User register(RegisterRequest request, User.Role role) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword()); // In production: Hash the password!
        user.setRole(role);
        
        return userRepository.save(user);
    }
    
    public Optional<User> login(LoginRequest request) {
        return userRepository.findByUsername(request.getUsername())
                .filter(user -> user.getPassword().equals(request.getPassword()));
    }
    
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}
