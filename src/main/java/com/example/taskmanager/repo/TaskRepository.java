package com.example.taskmanager.repo;

import com.example.taskmanager.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    // find all tasks for a specific project
    List<Task> findByProjectId(Long projectId);

    // find a task by ID only if it belongs to a project owner by the current user
    Optional<Task> findByIdAndProjectOwnerUsername(Long id, String username);

}
