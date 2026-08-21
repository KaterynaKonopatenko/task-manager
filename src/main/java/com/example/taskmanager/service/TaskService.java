package com.example.taskmanager.service;

import com.example.taskmanager.exception.ResourceNotFoundException;
import com.example.taskmanager.model.*;
import com.example.taskmanager.repo.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // create a new task and assing it to the current user
    @Transactional
    public Task create(String title, String description, String priority, LocalDateTime dueDate, Project project, User assignedTo) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        // convert string to enum prevents saving invalid priority values
        task.setPriority(TaskPriority.valueOf(priority));
        task.setDueDate(dueDate);
        task.setProject(project);
        task.setAssignedTo(assignedTo);
        return taskRepository.save(task);
    }

    // get all tasks for a specific project
    @Transactional(readOnly = true)
    public List<Task> getByProject(Long projectId) {
        return taskRepository.findByProjectId(projectId);
    }

    // paginated + sorted + optionally filtred by status for the project page
    @Transactional(readOnly = true)
    public Page<Task> getByProject(Long projectId, TaskStatus status,Pageable pageable) {
        if(status != null) {
            return taskRepository.findByProjectIdAndStatus(projectId, status, pageable);
        }
        return taskRepository.findByProjectId(projectId, pageable);
    }

    // get task by ID only if it belongs to a project owner by the current user
    @Transactional(readOnly = true)
    public Optional<Task> getByIdAndOwner(Long id, String username) {
        return taskRepository.findByIdAndProjectOwnerUsername(id, username);
    }

    // update task status only if it belongs to the current user
    @Transactional
    public Task updateStatus(Long id, String status, String username) {
        Task task = taskRepository.findByIdAndProjectOwnerUsername(id, username)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found or access denied"));
        // convert string to enum prevents saving invalid status valued
        task.setStatus(TaskStatus.valueOf(status));
        return taskRepository.save(task);
    }

    // delete task only if it belongs to the current user
    @Transactional
    public void delete(Long id, String username) {
        Task task = taskRepository.findByIdAndProjectOwnerUsername(id, username)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found or access denied"));
        taskRepository.delete(task);
    }
}
