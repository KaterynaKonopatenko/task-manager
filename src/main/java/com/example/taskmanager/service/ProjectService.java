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

    public Project create(String name, String description, User owner) {
        Project project = new Project();
        project.setName(name);
        project.setDescription(description);
        project.setOwner(owner);
        return projectRepository.save(project);
    }

    public List<Project> getByOwner(Long ownerId) {
        return projectRepository.findByOwnerId(ownerId);
    }

    public Optional<Project> getById(Long id) {
        return projectRepository.findById(id);
    }

    public void delete(Long id) {
        projectRepository.deleteById(id);
    }

    public Project update(Long id, String name, String description) {
        Project project = projectRepository.findById(id).orElseThrow();
        project.setName(name);
        project.setDescription(description);
        return projectRepository.save(project);
    }
}
