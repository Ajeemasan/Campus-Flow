
package com.campusflow.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.campusflow.service.UserService;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String password,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            userService.registerUser(fullName, email, password);
            redirectAttributes.addFlashAttribute(
                "success",
                "Registration successful! Please log in."
            );
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("fullName", fullName);
            model.addAttribute("email", email);
            return "register";
        }
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }
}
