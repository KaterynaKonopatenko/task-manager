package com.example.taskmanager.controller;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.Project;
import com.example.taskmanager.model.User;
import com.example.taskmanager.service.CommentService;
import com.example.taskmanager.service.ProjectService;
import com.example.taskmanager.service.TaskService;
import com.example.taskmanager.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final ProjectService projectService;
    private final UserService userService;
    private final CommentService commentService;

    public TaskController(TaskService taskService, ProjectService projectService, UserService userService, CommentService commentService) {
        this.taskService = taskService;
        this.projectService = projectService;
        this.userService = userService;
        this.commentService = commentService;
    }

    @PostMapping("/create")
    public String create(@RequestParam String title, @RequestParam String description, @RequestParam String priority, @RequestParam Long projectId, @AuthenticationPrincipal UserDetails userDetails) {
        Project project = projectService.getById(projectId).orElseThrow();
        User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();
        taskService.create(title,description,priority,null,project,user);
        return "redirect:/projects/" + projectId;
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model,@AuthenticationPrincipal UserDetails userDetails) {
        Task task = taskService.getById(id).orElseThrow();
        User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();
        model.addAttribute("task", task);
        model.addAttribute("comments",commentService.getByTask(id));
        model.addAttribute("user", user);
        return "task";
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id, @RequestParam String status) {
        Task task = taskService.getById(id).orElseThrow();
        taskService.updateStatus(id, status);
        return "redirect:/tasks/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        Task task = taskService.getById(id).orElseThrow();
        Long projectId = task.getProject().getId();
        taskService.delete(id);
        return "redirect:/projects/" + projectId;
    }

    @PostMapping("/{id}/comment")
    public String addComment(@PathVariable Long id, @RequestParam String text, @AuthenticationPrincipal UserDetails userDetails) {
        Task task = taskService.getById(id).orElseThrow();
        User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();
        commentService.add(text,task,user);
        return "redirect:/tasks/" + id;
    }
}
