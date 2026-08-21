package com.example.taskmanager.controller;


import com.example.taskmanager.model.Project;
import com.example.taskmanager.model.User;
import com.example.taskmanager.service.ProjectService;
import com.example.taskmanager.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Controller
public class DashboardController {

    private final ProjectService projectService;
    private final UserService userService;

    public DashboardController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "name") String sortBy,
                            @RequestParam(defaultValue = "asc") String direction) {
        User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();

        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, 6, sort);
        Page<Project> projectPage = projectService.getByOwner(user.getId(), pageable);

        model.addAttribute("projectPage", projectPage);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);
        model.addAttribute("user", user);
        return "dashboard";
    }
}
