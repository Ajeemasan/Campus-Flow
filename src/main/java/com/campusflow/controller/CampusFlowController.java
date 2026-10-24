
package com.campusflow.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.campusflow.model.CampusTask;
import com.campusflow.model.User;
import com.campusflow.repository.CampusTaskRepository;
import com.campusflow.repository.UserRepository;

import jakarta.validation.Valid;

@Controller
public class CampusFlowController {

    private final CampusTaskRepository repository;
    private final UserRepository userRepository;

    public CampusFlowController(CampusTaskRepository repository,
                                UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser(Authentication authentication) {
        return userRepository.findByEmail(
                authentication.getName().trim().toLowerCase()
        ).orElseThrow(() -> new IllegalStateException("Logged-in user not found"));
    }

    @GetMapping("/")
    public String dashboard(
            @RequestParam(required = false) String category,
            Model model,
            Authentication authentication) {

        User user = getCurrentUser(authentication);
        Long userId = user.getId();

        List<CampusTask> tasks;

        if (category == null || category.isBlank()
                || category.equalsIgnoreCase("ALL")) {
            tasks = repository.findByUser_IdOrderByDueDateAsc(userId);
        } else {
            tasks = repository.findByUser_IdAndCategoryIgnoreCaseOrderByDueDateAsc(
                    userId, category);
        }

        List<CampusTask> allTasks =
                repository.findByUser_IdOrderByDueDateAsc(userId);

        model.addAttribute("tasks", tasks);
        model.addAttribute("selectedCategory",
                category == null ? "ALL" : category.toUpperCase());
        model.addAttribute("totalCount", allTasks.size());
        model.addAttribute("pendingCount",
                allTasks.stream().filter(t -> !t.isCompleted()).count());
        model.addAttribute("completedCount",
                allTasks.stream().filter(CampusTask::isCompleted).count());
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("taskForm", new CampusTask());

        return "index";
    }

    @PostMapping("/tasks")
    public String addTask(
            @Valid @ModelAttribute("taskForm") CampusTask task,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Authentication authentication) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                    "error", "Please enter a title, category and due date.");
            return "redirect:/";
        }

        User user = getCurrentUser(authentication);
        task.setId(null);
        task.setUser(user);
        repository.save(task);

        redirectAttributes.addFlashAttribute(
                "message", "Your item was added to CampusFlow.");

        return "redirect:/";
    }

    @PostMapping("/tasks/{id}/toggle")
    public String toggleTask(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes,
            Authentication authentication) {

        User user = getCurrentUser(authentication);

        repository.findById(id)
                .filter(task -> task.getUser() != null
                        && task.getUser().getId().equals(user.getId()))
                .ifPresent(task -> {
                    task.setCompleted(!task.isCompleted());
                    repository.save(task);
                });

        redirectAttributes.addFlashAttribute(
                "message", "Task status updated if the item belongs to your account.");

        return "redirect:/";
    }

    @PostMapping("/tasks/{id}/delete")
    public String deleteTask(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes,
            Authentication authentication) {

        User user = getCurrentUser(authentication);

        repository.findById(id)
                .filter(task -> task.getUser() != null
                        && task.getUser().getId().equals(user.getId()))
                .ifPresent(repository::delete);

        redirectAttributes.addFlashAttribute(
                "message", "Item deleted if it belonged to your account.");

        return "redirect:/";
    }
}
