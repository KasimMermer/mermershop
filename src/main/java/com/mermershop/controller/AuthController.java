package com.mermershop.controller;

import com.mermershop.dto.LoginDto;
import com.mermershop.dto.RegisterDto;
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
        model.addAttribute("loginRequest", new LoginDto());
        return "login";
    }
    
    @PostMapping("/login")
    public String login(@ModelAttribute LoginDto request, HttpSession session, Model model) {
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
                    model.addAttribute("error", "Ungültiger Benutzername oder Passwort.");
                    return "login";
                });
    }
    
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerRequest", new RegisterDto());
        return "register";
    }
    
    @PostMapping("/register")
    public String register(@ModelAttribute RegisterDto request, Model model) {
        try {
            userService.register(request, User.Role.USER);
            model.addAttribute("success", "Registrierung erfolgreich! Bitte einloggen.");
            return "login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
