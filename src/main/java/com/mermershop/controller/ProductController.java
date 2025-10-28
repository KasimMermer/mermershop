package com.mermershop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.mermershop.dto.ProductDto;
import com.mermershop.model.Product;
import com.mermershop.service.CartService;
import com.mermershop.service.ProductService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/products")
public class ProductController {

     @Autowired
    private HttpSession session;

    @Autowired
    private ProductService productService;
    
    @Autowired
    private CartService cartService;
    
    @GetMapping
    public String showProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("role", session.getAttribute("role"));
        return "products";
    }
    
    @GetMapping("/detail/{id}")
    public String showProductDetail(@PathVariable Long id, HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");
        
        if (username == null) {
            return "redirect:/login";
        }
        
        try {
            Product product = productService.getProductById(id);
            model.addAttribute("username", username);
            model.addAttribute("role", role);
            model.addAttribute("product", product);
            model.addAttribute("itemCount", cartService.getItemCount());
            return "product-detail";
        } catch (RuntimeException e) {
            String redirectUrl = "ADMIN".equals(role) ? "/admin/dashboard" : "/user/dashboard";
            return "redirect:" + redirectUrl + "?error=" + e.getMessage();
        }
    }
    
    @GetMapping("/add")
    public String showAddProductForm(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");
        
        if (username == null || !"ADMIN".equals(role)) {
            return "redirect:/user-dashboard";
        }
        
        model.addAttribute("username", username);
        model.addAttribute("productDto", new ProductDto());
        return "add-product";
    }
    
    @PostMapping("/add")
    public String addProduct(ProductDto productDto, @RequestParam("image") MultipartFile image) {
        try {
            productService.addProduct(productDto, image);
            return "redirect:/admin/dashboard?success=true";
        } catch (RuntimeException e) {
            return "redirect:/admin/dashboard?error=" + e.getMessage();
        }
    }
    
    @GetMapping("/edit/{id}")
    public String showEditProductForm(@PathVariable Long id, HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");
        
        if (username == null || !"ADMIN".equals(role)) {
            return "redirect:/login";
        }
        
        try {
            Product product = productService.getProductById(id);
            ProductDto productDto = new ProductDto();
            productDto.setName(product.getName());
            productDto.setDescription(product.getDescription());
            productDto.setPrice(product.getPrice());
            
            model.addAttribute("username", username);
            model.addAttribute("productDto", productDto);
            model.addAttribute("productId", id);
            model.addAttribute("currentImageUrl", product.getImageUrl());
            return "edit-product";
        } catch (RuntimeException e) {
            return "redirect:/admin/dashboard?error=" + e.getMessage();
        }
    }
    
    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable Long id, ProductDto productDto, 
                               @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            productService.updateProduct(id, productDto, image);
            return "redirect:/admin/dashboard?success=true";
        } catch (RuntimeException e) {
            return "redirect:/admin/dashboard?error=" + e.getMessage();
        }
    }
    
    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return "redirect:/admin/dashboard?success=true";
        } catch (RuntimeException e) {
            return "redirect:/admin/dashboard?error=" + e.getMessage();
        }
    }

}
