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

    public boolean addProduct(ProductDto productDto, MultipartFile image) {
        if(session.getAttribute("role") == null || !"ADMIN".equals(session.getAttribute("role"))) {
            throw new RuntimeException("Nur Administratoren können Produkte hinzufügen.");
        }

        if (productRepository.existsByName(productDto.getName())) {
            throw new RuntimeException("Produkt existiert bereits.");
        }
        
        // Validiere Bildformat
        String contentType = image.getContentType();
        if (contentType == null || 
            (!contentType.equals("image/jpeg") && 
             !contentType.equals("image/png") && 
             !contentType.equals("image/webp"))) {
            throw new RuntimeException("Nur JPG, PNG und WEBP Bilder sind erlaubt.");
        }
        
        // Speichere das Bild
        String imageUrl = saveImage(image);

        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setImageUrl(imageUrl);

        productRepository.save(product);
        return true;
    }
    
    private String saveImage(MultipartFile file) {
        try {
            // Erstelle images Ordner falls nicht vorhanden
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            // Generiere eindeutigen Dateinamen
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String filename = UUID.randomUUID().toString() + extension;
            
            // Speichere Datei
            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            // Rückgabe: relativer Pfad für HTML
            return "/images/" + filename;
            
        } catch (IOException e) {
            throw new RuntimeException("Fehler beim Speichern des Bildes: " + e.getMessage());
        }
    }
}
