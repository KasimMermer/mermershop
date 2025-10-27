package com.mermershop.config;

import com.mermershop.model.User;
import com.mermershop.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {
    
    @Bean
    public CommandLineRunner seedData(UserRepository userRepository) {
        return args -> {
            // Check if admin already exists
            if (!userRepository.existsByUsername("admin")) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword("12340000");
                admin.setRole(User.Role.ADMIN);
                userRepository.save(admin);
            }
            
            // Check if user already exists
            if (!userRepository.existsByUsername("user")) {
                User user = new User();
                user.setUsername("user");
                user.setPassword("12340000");
                user.setRole(User.Role.USER);
                userRepository.save(user);
            }
        };
    }
}
