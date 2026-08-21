package com.example.taskmanager.dto;

import com.example.taskmanager.model.Project;

public class ProjectSummaryDto {
    private final Long id;
    private final String name;
    private final String description;

    public ProjectSummaryDto(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public static ProjectSummaryDto from(Project project) {
        return new ProjectSummaryDto(project.getId(), project.getName(), project.getDescription());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
