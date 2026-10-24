
package com.campusflow.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HexFormat;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.campusflow.model.User;
import com.campusflow.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final SecureRandom secureRandom = new SecureRandom();

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
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
                "An account with this email already exists. If you haven't verified it, use the OTP resend option."
            );
        }

        User user = new User();
        user.setFullName(fullName.trim());
        user.setEmail(normalizedEmail);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmailVerified(false);

        String otp = generateOtp();
       user.setOtpHash(hashOtp(otp));
user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
user.setOtpSentAt(LocalDateTime.now());
user.setOtpAttempts(0);
        userRepository.save(user);
        emailService.sendOtp(normalizedEmail, otp);
    }

    @Transactional
    public void verifyOtp(String email, String otp) {
        User user = findUser(email);

        if (user.getOtpSentAt() != null &&
        LocalDateTime.now().isBefore(user.getOtpSentAt().plusSeconds(60))) {
    throw new IllegalArgumentException(
        "Please wait 60 seconds between OTP requests."
    );
}

        if (user.getOtpHash() == null || user.getOtpExpiry() == null) {
            throw new IllegalArgumentException("No active OTP. Please request a new one.");
        }

        if (user.getOtpAttempts() >= 5) {
            user.setOtpHash(null);
            user.setOtpExpiry(null);
            userRepository.save(user);
            throw new IllegalArgumentException("Too many incorrect attempts. Please request a new OTP.");
        }

        if (LocalDateTime.now().isAfter(user.getOtpExpiry())) {
            user.setOtpHash(null);
            user.setOtpExpiry(null);
            userRepository.save(user);
            throw new IllegalArgumentException("OTP expired. Please request a new one.");
        }

        if (otp == null || !MessageDigest.isEqual(
                hashOtp(otp).getBytes(StandardCharsets.UTF_8),
                user.getOtpHash().getBytes(StandardCharsets.UTF_8))) {
            user.setOtpAttempts(user.getOtpAttempts() + 1);
            userRepository.save(user);
            throw new IllegalArgumentException("Incorrect OTP. Please try again.");
        }

        user.setEmailVerified(true);
        user.setOtpHash(null);
        user.setOtpExpiry(null);
        user.setOtpAttempts(0);
        userRepository.save(user);
    }

    @Transactional
    public void resendOtp(String email) {
        User user = findUser(email);

        if (user.isEmailVerified()) {
            throw new IllegalArgumentException("This email is already verified. Please log in.");
        }

        String otp = generateOtp();
       user.setOtpHash(hashOtp(otp));
user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
user.setOtpSentAt(LocalDateTime.now());
user.setOtpAttempts(0);

        userRepository.save(user);
        emailService.sendOtp(user.getEmail(), otp);
    }

    private User findUser(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required.");
        }

        return userRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException(
                    "Registration not found. Please register first."
                ));
    }

    private String generateOtp() {
        return String.format("%06d", secureRandom.nextInt(1_000_000));
    }

    private String hashOtp(String otp) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(
                otp.getBytes(StandardCharsets.UTF_8)
            );
            return HexFormat.of().formatHex(hash);
        } catch (Exception e) {
            throw new IllegalStateException("Could not process OTP.");
        }
    }
}
