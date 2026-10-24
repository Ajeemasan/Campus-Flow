package com.campusflow.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class CampusTask {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
@JoinColumn(name = "user_id")
private User user;
 
@NotBlank
    private String title;

    @Column(length = 1000)
    private String description;

    @NotBlank
    private String category;

    @NotNull
    private LocalDate dueDate;

    private String assignedTo;
    private boolean completed = false;

    public CampusTask() {}

    public CampusTask(String title, String description, String category, LocalDate dueDate, String assignedTo) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.dueDate = dueDate;
        this.assignedTo = assignedTo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public String getAssignedTo() { return assignedTo; }
    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    public User getUser() {
    return user;
}

public void setUser(User user) {
    this.user = user;
}
}
