
package com.campusflow.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.campusflow.model.CampusTask;
import com.campusflow.model.User;
import com.campusflow.repository.CampusTaskRepository;

@Service
public class CampusTaskReminderService {

    private final CampusTaskRepository taskRepository;
    private final EmailService emailService;

    public CampusTaskReminderService(
            CampusTaskRepository taskRepository,
            EmailService emailService) {
        this.taskRepository = taskRepository;
        this.emailService = emailService;
    }

    @Scheduled(cron = "0 0 9 * * *")
    @Transactional
    public void sendDueDateReminders() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        List<CampusTask> tasks =
                taskRepository.findByDueDateAndCompletedFalseAndReminderSentFalse(
                        tomorrow);

        for (CampusTask task : tasks) {
            User user = task.getUser();

            if (user == null || user.getEmail() == null
                    || user.getEmail().isBlank()) {
                continue;
            }

            try {
                emailService.sendTaskReminder(user.getEmail(), task);
                task.setReminderSent(true);
                taskRepository.save(task);

                System.out.println(
                        "Reminder sent for task ID: " + task.getId());
            } catch (Exception e) {
                System.err.println(
                        "Failed to send reminder for task ID: "
                                + task.getId());
            }
        }
    }
}
