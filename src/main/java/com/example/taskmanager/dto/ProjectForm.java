package com.example.taskmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// form object for creating a project with validation rules
public class ProjectForm {

    @NotBlank(message = "Project name is required")
    @Size(min = 2, max = 50, message = "Project name must be 2-50 characters")
    private String name;

    @Size(max = 200, message = "Description must be under 200 characters")
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
