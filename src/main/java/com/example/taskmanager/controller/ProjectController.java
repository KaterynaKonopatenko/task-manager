package com.example.taskmanager.controller;


import com.example.taskmanager.exception.ResourceNotFoundException;
import com.example.taskmanager.model.Project;
import com.example.taskmanager.model.User;
import com.example.taskmanager.service.ProjectService;
import com.example.taskmanager.service.UserService;
import com.example.taskmanager.dto.ProjectForm;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;

    public ProjectController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }

    // create a new project for the current user; validated via Projectfoem before saving
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("projectFrom") ProjectForm form, BindingResult result, @AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", result.getFieldError().getDefaultMessage());
            return "redirect:/dashboard";
        }
        User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();
        projectService.create(form.getName(), form.getDescription(), user);
        return "redirect:/dashboard";
    }

    // open a project only if it belong to the current user
    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Project project = projectService.getByIdAndOwner(id, userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found or access denied"));
        model.addAttribute("project", project);
        return "project";
    }

    // delete a project only if it belongs to the current user
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        projectService.delete(id, userDetails.getUsername());
        return "redirect:/dashboard";
    }
}
