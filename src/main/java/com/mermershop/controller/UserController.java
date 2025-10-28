package com.mermershop.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mermershop.service.ProductService;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private ProductService productService;
    
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");
        
        if (username == null) {
            return "redirect:/login";
        }

        var products = productService.getAllProducts();
        
        model.addAttribute("username", username);
        model.addAttribute("role", role);
        model.addAttribute("products", products);
        return "user-dashboard";
    }
}
