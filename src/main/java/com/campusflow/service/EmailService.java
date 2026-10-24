
package com.campusflow.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtp(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("CampusFlow - Email Verification OTP");
        message.setText(
            "Hello,\n\n" +
            "Your CampusFlow registration OTP is: " + otp + "\n\n" +
            "This OTP expires in 5 minutes.\n\n" +
            "If you did not request this, please ignore this email.\n\n" +
            "Regards,\nCampusFlow Team"
        );

        mailSender.send(message);
    }
}
