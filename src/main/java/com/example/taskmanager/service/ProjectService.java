package com.example.taskmanager.service;

import com.example.taskmanager.model.Project;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repo.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    // create a new project and set the current user as owner
    public Project create(String name, String description, User owner) {
        Project project = new Project();
        project.setName(name);
        project.setDescription(description);
        project.setOwner(owner);
        return projectRepository.save(project);
    }

    // get all projects that belong to the current user
    public List<Project> getByOwner(Long ownerId) {
        return projectRepository.findByOwnerId(ownerId);
    }

    // get project by ID only if it belongs to the current user
    public Optional<Project> getByIdAndOwner(Long id, String username) {
        return projectRepository.findByIdAndOwnerUsername(id, username);
    }

    // delete  project only if it belongs to the current user
    public void delete(Long id, String username) {
        Project project = projectRepository.findByIdAndOwnerUsername(id, username)
                .orElseThrow(() -> new RuntimeException("Project not found or access denied"));
        projectRepository.delete(project);
    }
}
