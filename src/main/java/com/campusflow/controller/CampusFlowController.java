package com.campusflow.controller;

import com.campusflow.model.CampusTask;
import com.campusflow.repository.CampusTaskRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;

@Controller
public class CampusFlowController {
    private final CampusTaskRepository repository;

    public CampusFlowController(CampusTaskRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public String dashboard(@RequestParam(required = false) String category, Model model) {
        var tasks = (category == null || category.isBlank() || category.equalsIgnoreCase("ALL"))
                ? repository.findAllByOrderByDueDateAsc()
                : repository.findByCategoryIgnoreCaseOrderByDueDateAsc(category);
        model.addAttribute("tasks", tasks);
        model.addAttribute("selectedCategory", category == null ? "ALL" : category.toUpperCase());
        model.addAttribute("totalCount", repository.count());
        model.addAttribute("pendingCount", repository.findAll().stream().filter(t -> !t.isCompleted()).count());
        model.addAttribute("completedCount", repository.findAll().stream().filter(CampusTask::isCompleted).count());
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("taskForm", new CampusTask());
        return "index";
    }

    @PostMapping("/tasks")
    public String addTask(@Valid @ModelAttribute("taskForm") CampusTask task,
                          BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Please enter a title, category and due date.");
            return "redirect:/";
        }
        repository.save(task);
        redirectAttributes.addFlashAttribute("message", "Your item was added to CampusFlow.");
        return "redirect:/";
    }

    @PostMapping("/tasks/{id}/toggle")
    public String toggleTask(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        repository.findById(id).ifPresent(task -> {
            task.setCompleted(!task.isCompleted());
            repository.save(task);
        });
        redirectAttributes.addFlashAttribute("message", "Task status updated.");
        return "redirect:/";
    }

    @PostMapping("/tasks/{id}/delete")
    public String deleteTask(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        repository.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Item deleted.");
        return "redirect:/";
    }
}
