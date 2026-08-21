package com.example.taskmanager.dto;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.TaskStatus;
import com.example.taskmanager.model.TaskPriority;

import java.time.LocalDateTime;

public class TaskDetailDto {
    private final Long id;
    private final String title;
    private final String description;
    private final TaskStatus status;
    private final TaskPriority priority;
    private final LocalDateTime dueDate;
    private final Long projectId;

    public TaskDetailDto(Long id, String title, String description, TaskStatus status, TaskPriority priority, LocalDateTime dueDate, Long projectId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.dueDate = dueDate;
        this.projectId = projectId;
    }

    public static TaskDetailDto from(Task task) {
        return new TaskDetailDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getDueDate(),
                task.getProject().getId()
        );
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public Long getProjectId() {
        return projectId;
    }
}
