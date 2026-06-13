package com.example.taskmanager.service;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;
import com.example.taskmanager.model.Project;
import com.example.taskmanager.repo.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task create(String title, String description, String priority, LocalDateTime dueDate, Project project, User assignedTo) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setPriority(priority);
        task.setDueDte(dueDate);
        task.setProject(project);
        task.setAssignedTo(assignedTo);
        return taskRepository.save(task);
    }

    public List<Task> getByProject(Long projectId) {
        return taskRepository.findByProjectId(projectId);
    }

    public Optional<Task> getById(Long id) {
        return taskRepository.findById(id);
    }

    public Task updateStatus(Long id, String status) {
        Task task = taskRepository.findById(id).orElseThrow();
        task.setStatus(status);
        return taskRepository.save(task);
    }

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    public Task update(Long id, String title, String description, String priority, String status, LocalDateTime dueDate) {
        Task task = taskRepository.findById(id).orElseThrow();
        task.setTitle(title);
        task.setDescription(description);
        task.setPriority(priority);
        task.setStatus(status);
        task.setDueDte(dueDate);
        return taskRepository.save(task);
    }
}
