package com.example.taskmanager.repo;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    // paginated + sorted list of tasks for the project page
    Page<Task> findByProjectId(Long projectId, Pageable pageable);

    // same but filtered to only one status
    Page<Task> findByProjectIdAndStatus(Long projectId, TaskStatus status, Pageable pageable);

}
