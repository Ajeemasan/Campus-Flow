
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
            Model model) {

        try {
            userService.registerUser(fullName, email, password);
            return "redirect:/verify-otp?email=" +
                    java.net.URLEncoder.encode(
                        email.trim().toLowerCase(),
                        java.nio.charset.StandardCharsets.UTF_8
                    );
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("fullName", fullName);
            model.addAttribute("email", email);
            return "register";
        }
    }

    @GetMapping("/verify-otp")
    public String showOtpPage(@RequestParam(required = false) String email,
                              Model model) {
        model.addAttribute("email", email == null ? "" : email);
        return "verify-otp";
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(
            @RequestParam String email,
            @RequestParam String otp,
            RedirectAttributes redirectAttributes,
            Model model) {

        try {
            userService.verifyOtp(email, otp);
            redirectAttributes.addFlashAttribute(
                "success",
                "Email verified successfully! You can now log in."
            );
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("email", email);
            return "verify-otp";
        }
    }

    @PostMapping("/resend-otp")
    public String resendOtp(
            @RequestParam String email,
            RedirectAttributes redirectAttributes) {

        try {
            userService.resendOtp(email);
            redirectAttributes.addFlashAttribute(
                "success",
                "A new OTP has been sent to your email."
            );
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/verify-otp?email=" +
                java.net.URLEncoder.encode(
                    email.trim().toLowerCase(),
                    java.nio.charset.StandardCharsets.UTF_8
                );
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }
}
