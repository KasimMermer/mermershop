package com.mermershop.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    
    private static final String UPLOAD_DIR = "src/main/resources/static/images/";

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    public Product getProductById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Produkt nicht gefunden."));
    }

    public boolean addProduct(ProductDto productDto, MultipartFile image) {
        if(session.getAttribute("role") == null || !"ADMIN".equals(session.getAttribute("role"))) {
            throw new RuntimeException("Nur Administratoren können Produkte hinzufügen.");
        }

        if (productRepository.existsByName(productDto.getName())) {
            throw new RuntimeException("Produkt existiert bereits.");
        }
        
        String contentType = image.getContentType();
        if (contentType == null || 
            (!contentType.equals("image/jpeg") && 
             !contentType.equals("image/png") && 
             !contentType.equals("image/webp"))) {
            throw new RuntimeException("Nur JPG, PNG und WEBP Bilder sind erlaubt.");
        }
        
        String imageUrl = saveImage(image);

        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setImageUrl(imageUrl);

        productRepository.save(product);
        return true;
    }
    
    public boolean updateProduct(Long id, ProductDto productDto, MultipartFile image) {
        if(session.getAttribute("role") == null || !"ADMIN".equals(session.getAttribute("role"))) {
            throw new RuntimeException("Nur Administratoren können Produkte bearbeiten.");
        }
        
        Product product = getProductById(id);
        
        if (!product.getName().equals(productDto.getName()) && 
            productRepository.existsByName(productDto.getName())) {
            throw new RuntimeException("Produktname wird bereits verwendet.");
        }
        
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        
        if (image != null && !image.isEmpty()) {
            String contentType = image.getContentType();
            if (contentType == null || 
                (!contentType.equals("image/jpeg") && 
                 !contentType.equals("image/png") && 
                 !contentType.equals("image/webp"))) {
                throw new RuntimeException("Nur JPG, PNG und WEBP Bilder sind erlaubt.");
            }
            
            if (product.getImageUrl() != null) {
                deleteImage(product.getImageUrl());
            }
            
            String imageUrl = saveImage(image);
            product.setImageUrl(imageUrl);
        }
        
        productRepository.save(product);
        return true;
    }
    
    public boolean deleteProduct(Long id) {
        if(session.getAttribute("role") == null || !"ADMIN".equals(session.getAttribute("role"))) {
            throw new RuntimeException("Nur Administratoren können Produkte löschen.");
        }
        
        Product product = getProductById(id);
        
        if (product.getImageUrl() != null) {
            deleteImage(product.getImageUrl());
        }
        
        productRepository.deleteById(id);
        return true;
    }
    
    private String saveImage(MultipartFile file) {
        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String filename = UUID.randomUUID().toString() + extension;
            
            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            return "/images/" + filename;
            
        } catch (IOException e) {
            throw new RuntimeException("Fehler beim Speichern des Bildes: " + e.getMessage());
        }
    }
    
    private void deleteImage(String imageUrl) {
        try {
            String filename = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
            Path filePath = Paths.get(UPLOAD_DIR + filename);
            
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
        } catch (IOException e) {
            System.err.println("Warnung: Bild konnte nicht gelöscht werden: " + e.getMessage());
        }
    }
}
