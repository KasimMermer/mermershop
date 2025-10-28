package com.mermershop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mermershop.dto.ProductDto;
import com.mermershop.model.Product;
import com.mermershop.repository.ProductRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private HttpSession session;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public boolean addProduct(ProductDto productDto) {
        if(session.getAttribute("role") == null || !"ADMIN".equals(session.getAttribute("role"))) {
            throw new RuntimeException("Nur Administratoren können Produkte hinzufügen.");
        }

        if (productRepository.existsByName(productDto.getName())) {
            throw new RuntimeException("Produkt existiert bereits.");
        }

        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());

        productRepository.save(product);
        return true;
    }
}
