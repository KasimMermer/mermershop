package com.mermershop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mermershop.service.CartService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class CartController {
    
    @Autowired
    private CartService cartService;
    
    @GetMapping
    public String showCart(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");
        
        if (username == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("username", username);
        model.addAttribute("role", role);
        model.addAttribute("cartItems", cartService.getCartItems());
        model.addAttribute("total", cartService.getTotal());
        model.addAttribute("itemCount", cartService.getItemCount());
        
        return "cart";
    }
    
    @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable Long productId, 
                           @RequestParam(defaultValue = "1") Integer quantity) {
        try {
            cartService.addToCart(productId, quantity);
            return "redirect:/user/dashboard?cartSuccess=true";
        } catch (RuntimeException e) {
            return "redirect:/user/dashboard?error=" + e.getMessage();
        }
    }
    
    @PostMapping("/update/{productId}")
    public String updateQuantity(@PathVariable Long productId, 
                                @RequestParam Integer quantity) {
        try {
            if (quantity <= 0) {
                cartService.removeFromCart(productId);
            } else {
                cartService.updateQuantity(productId, quantity);
            }
            return "redirect:/cart";
        } catch (RuntimeException e) {
            return "redirect:/cart?error=" + e.getMessage();
        }
    }
    
    @PostMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId) {
        try {
            cartService.removeFromCart(productId);
            return "redirect:/cart?removed=true";
        } catch (RuntimeException e) {
            return "redirect:/cart?error=" + e.getMessage();
        }
    }
    
    @PostMapping("/clear")
    public String clearCart() {
        cartService.clearCart();
        return "redirect:/cart?cleared=true";
    }
}
