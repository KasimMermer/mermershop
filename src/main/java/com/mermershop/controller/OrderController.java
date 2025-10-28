package com.mermershop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mermershop.service.OrderService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/orders")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    @GetMapping
    public String showOrders(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");
        
        if (userId == null) {
            return "redirect:/login";
        }
        
        var orders = orderService.getUserOrders(userId);
        
        model.addAttribute("username", username);
        model.addAttribute("role", role);
        model.addAttribute("orders", orders);
        
        return "orders";
    }
    
    @PostMapping("/checkout")
    public String checkout(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        
        if (userId == null) {
            return "redirect:/login";
        }
        
        try {
            orderService.createOrder(userId);
            return "redirect:/orders?success=true";
        } catch (RuntimeException e) {
            return "redirect:/cart?error=" + e.getMessage();
        }
    }
}
