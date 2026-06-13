package com.example.taskmanager.controller;


import com.example.taskmanager.model.Project;
import com.example.taskmanager.model.User;
import com.example.taskmanager.service.ProjectService;
import com.example.taskmanager.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;

    public ProjectController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }


    @PostMapping("/create")
    public String create(@RequestParam String name, @RequestParam String description, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();
        projectService.create(name, description, user);
        return "redirect:/dashboard";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        Project project = projectService.getById(id).orElseThrow();
        model.addAttribute("project", project);
        return "project";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        projectService.delete(id);
        return "redirect:/dashboard";
    }
}
