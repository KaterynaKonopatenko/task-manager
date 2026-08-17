package com.example.taskmanager.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// form object for creating a task with validation rules
public class TaskForm {

    @NotBlank(message = "Task title is required")
    @Size(min = 2, max = 100, message = "Title must be 2-100 characters")
    private String title;

    @Size(max = 500, message = "Description must be under 500 characters")
    private String description;

    private String priority = "MEDIUM";

    private Long projectId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.time.LocalDate dueDate;

    public java.time.LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(java.time.LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
