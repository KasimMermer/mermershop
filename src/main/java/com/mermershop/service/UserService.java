package com.mermershop.service;

import com.mermershop.dto.LoginDto;
import com.mermershop.dto.RegisterDto;
import com.mermershop.enums.UserRoleEnum;
import com.mermershop.model.User;
import com.mermershop.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    public User register(RegisterDto request, UserRoleEnum role) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Benutzername existiert bereits.");
        }
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setRole(role);
        
        return userRepository.save(user);
    }
    
    public Optional<User> login(LoginDto request) {
        return userRepository.findByUsername(request.getUsername())
                .filter(user -> user.getPassword().equals(request.getPassword()));
    }
    
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}
