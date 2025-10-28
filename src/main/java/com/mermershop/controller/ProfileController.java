package com.mermershop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mermershop.dto.ProfileDto;
import com.mermershop.model.User;
import com.mermershop.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public String showProfile(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");
        
        if (userId == null) {
            return "redirect:/login";
        }
        
        User user = userService.findById(userId)
            .orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden"));
        
        ProfileDto profileDto = new ProfileDto();
        profileDto.setFirstName(user.getFirstName());
        profileDto.setLastName(user.getLastName());
        profileDto.setBirthDate(user.getBirthDate());
        
        model.addAttribute("username", username);
        model.addAttribute("role", role);
        model.addAttribute("profileDto", profileDto);
        
        return "profile";
    }
    
    @PostMapping("/update")
    public String updateProfile(ProfileDto profileDto, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        
        if (userId == null) {
            return "redirect:/login";
        }
        
        try {
            userService.updateProfile(userId, profileDto);
            return "redirect:/profile?success=true";
        } catch (RuntimeException e) {
            return "redirect:/profile?error=" + e.getMessage();
        }
    }
}
