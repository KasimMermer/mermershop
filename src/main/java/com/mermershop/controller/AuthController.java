package com.mermershop.controller;

import com.mermershop.dto.LoginRequest;
import com.mermershop.dto.RegisterRequest;
import com.mermershop.model.User;
import com.mermershop.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
    
    private final UserService userService;
    
    public AuthController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }
    
    // LOGIN
    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }
    
    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest request, HttpSession session, Model model) {
        return userService.login(request)
                .map(user -> {
                    session.setAttribute("userId", user.getId());
                    session.setAttribute("username", user.getUsername());
                    session.setAttribute("role", user.getRole().toString());
                    
                    if (user.getRole() == User.Role.ADMIN) {
                        return "redirect:/admin/dashboard";
                    } else {
                        return "redirect:/user/dashboard";
                    }
                })
                .orElseGet(() -> {
                    model.addAttribute("error", "Invalid username or password");
                    return "login";
                });
    }
    
    // REGISTER
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }
    
    @PostMapping("/register")
    public String register(@ModelAttribute RegisterRequest request, Model model) {
        try {
            userService.register(request, User.Role.USER);
            model.addAttribute("success", "Registration successful! Please login.");
            return "login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
    
    // ADMIN REGISTER
    @GetMapping("/register/admin")
    public String adminRegisterPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register-admin";
    }
    
    @PostMapping("/register/admin")
    public String registerAdmin(@ModelAttribute RegisterRequest request, Model model) {
        try {
            userService.register(request, User.Role.ADMIN);
            model.addAttribute("success", "Admin registration successful! Please login.");
            return "login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register-admin";
        }
    }
    
    // LOGOUT
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
