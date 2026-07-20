package com.example.taskmanager.repo;

import com.example.taskmanager.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    // find all projects that belong to a specific user
    List<Project> findByOwnerId(Long ownerId);

    // find a project by ID only if it belongs to the current user (security check)
    Optional<Project> findByIdAndOwnerUsername(Long id, String username);
}
