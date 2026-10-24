
package com.campusflow.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.campusflow.service.CampusTaskReminderService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ReminderLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final CampusTaskReminderService reminderService;

    public ReminderLoginSuccessHandler(
            CampusTaskReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        try {
            reminderService.sendDueDateReminders();
        } catch (Exception e) {
            System.err.println("Could not process task reminders: " + e.getMessage());
        }

        response.sendRedirect("/");
    }
}
