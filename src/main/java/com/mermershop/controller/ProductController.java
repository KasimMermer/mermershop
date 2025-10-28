package com.mermershop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mermershop.dto.ProductDto;
import com.mermershop.service.ProductService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/products")
public class ProductController {

     @Autowired
    private HttpSession session;

    @Autowired
    private ProductService productService;
    
    @GetMapping
    public String showProducts(Model model) {


        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("role", session.getAttribute("role"));
        return "products";
    }
    
    @PostMapping("/add")
    public String addProduct(ProductDto productDto) {
        try {
            productService.addProduct(productDto);
            return "redirect:/admin/dashboard?success=true";
        } catch (RuntimeException e) {
            return "redirect:/admin/dashboard?error=" + e.getMessage();
        }
    }

}
