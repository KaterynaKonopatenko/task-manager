package com.example.taskmanager.repo;

import com.example.taskmanager.model.Project;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    // find all projects that belong to a specific user
    List<Project> findByOwnerId(Long ownerId);

   // eagerly load tasks together with the project, since project.html displays them
    @EntityGraph(attributePaths = "tasks")
    Optional<Project> findByIdAndOwnerUsername(Long id, String username);

    // paginated + sorted list of project for the dashboard
    Page<Project> findByOwnerId(Long ownerId, Pageable pageable);
}
