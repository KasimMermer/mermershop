package com.mermershop.controller;

import com.mermershop.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private ProductService productService;
    
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");
        
        if (username == null || !"ADMIN".equals(role)) {
            return "redirect:/login";
        }
        
        var products = productService.getAllProducts();
        
        model.addAttribute("username", username);
        model.addAttribute("role", role);
        model.addAttribute("products", products);
        return "admin-dashboard";
    }
}
