package com.example.taskmanager.repo;

import com.example.taskmanager.model.Task;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    // find all tasks for a specific project
    List<Task> findByProjectId(Long projectId);

    // eagerly load the parent project too, since task.html links back to it
    @EntityGraph(attributePaths = "project")
    Optional<Task> findByIdAndProjectOwnerUsername(Long id, String username);

}
