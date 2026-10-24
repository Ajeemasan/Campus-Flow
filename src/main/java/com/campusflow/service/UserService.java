
package com.campusflow.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.campusflow.model.User;
import com.campusflow.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void registerUser(String fullName, String email, String password) {
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Full name is required.");
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required.");
        }

        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException(
                "Password must contain at least 8 characters."
            );
        }

        String normalizedEmail = email.trim().toLowerCase();

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new IllegalArgumentException(
                "An account with this email already exists."
            );
        }

        User user = new User();
        user.setFullName(fullName.trim());
        user.setEmail(normalizedEmail);
        user.setPassword(passwordEncoder.encode(password));

        userRepository.save(user);
    }
}
