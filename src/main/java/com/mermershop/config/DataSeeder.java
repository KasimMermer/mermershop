package com.mermershop.config;

import com.mermershop.enums.UserRoleEnum;
import com.mermershop.model.Product;
import com.mermershop.model.User;
import com.mermershop.repository.ProductRepository;
import com.mermershop.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {
    
    @Bean
    public CommandLineRunner seedData(UserRepository userRepository, ProductRepository productRepository) {
        return args -> {
            if (!userRepository.existsByUsername("admin")) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword("12340000");
                admin.setRole(UserRoleEnum.ADMIN);
                userRepository.save(admin);
            }
            
            if (!userRepository.existsByUsername("user")) {
                User user = new User();
                user.setUsername("user");
                user.setPassword("12340000");
                user.setRole(UserRoleEnum.USER);
                userRepository.save(user);
            }

            if (!productRepository.existsByName("1")) {
                Product product = new Product();
                product.setName("1");
                product.setDescription("Beschreibung für Produkt 1");
                product.setPrice(100.0);
                product.setImageUrl("/images/1.webp");
                productRepository.save(product);
            }

            if (!productRepository.existsByName("2")) {
                Product product = new Product();
                product.setName("2");
                product.setDescription("Beschreibung für Produkt 2");
                product.setPrice(200.0);
                product.setImageUrl("/images/2.webp");
                productRepository.save(product);
            }

            if (!productRepository.existsByName("3")) {
                Product product = new Product();
                product.setName("3");
                product.setDescription("Beschreibung für Produkt 3");
                product.setPrice(300.0);
                product.setImageUrl("/images/3.webp");
                productRepository.save(product);
            }
        };
    }
}
