package com.example.taskmanager.controller;

import com.example.taskmanager.dto.TaskDetailDto;
import com.example.taskmanager.dto.CommentDto;
import com.example.taskmanager.dto.TaskForm;
import com.example.taskmanager.exception.ResourceNotFoundException;
import com.example.taskmanager.model.Project;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;
import com.example.taskmanager.service.CommentService;
import com.example.taskmanager.service.ProjectService;
import com.example.taskmanager.service.TaskService;
import com.example.taskmanager.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;

import java.time.LocalDateTime;

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

    // create a new task in a project that belongs to the current user; validated via TaskForm before saving
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("taskForm") TaskForm form, BindingResult result,@AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttributes) {
        if(result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", result.getFieldError().getDefaultMessage());
            return "redirect:/projects/" + form.getProjectId();
        }
        Project project = projectService.getByIdAndOwner(form.getProjectId(), userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found or access denied"));
        User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();
        // convert the date only form value into a null LocalDateTime or leave null if the user didn't set one
        LocalDateTime dueDateTime = form.getDueDate() != null ? form.getDueDate().atStartOfDay() : null;
        taskService.create(form.getTitle(), form.getDescription(), form.getPriority(), dueDateTime, project, user);
        return "redirect:/projects/" + form.getProjectId();
    }

    //view a task only if it belongs to the current user
    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Task task = taskService.getByIdAndOwner(id, userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found or access denied"));
        User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();
        model.addAttribute("task", TaskDetailDto.from(task));
        model.addAttribute("comments", commentService.getByTask(id).stream().map(CommentDto::from).toList());
        model.addAttribute("user", user);
        return "task";
    }

    // update task status only if it belongs to the current user
    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id, @RequestParam String status, @AuthenticationPrincipal UserDetails userDetails) {
        taskService.updateStatus(id, status, userDetails.getUsername());
        return "redirect:/tasks/" + id;
    }

    // delete task only if it belongs to the current user
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        Task task = taskService.getByIdAndOwner(id, userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found or access denied"));
        Long projectId = task.getProject().getId();
        taskService.delete(id, userDetails.getUsername());
        return "redirect:/projects/" + projectId;
    }

    // add a comment to a task only if the task belongs to the current user
    @PostMapping("/{id}/comment")
    public String addComment(@PathVariable Long id, @RequestParam String text, @AuthenticationPrincipal UserDetails userDetails) {
        Task task = taskService.getByIdAndOwner(id, userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found or access denied"));
        User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();
        commentService.add(text, task, user);
        return "redirect:/tasks/" + id;
    }

    // delete a comment but only if the task if belongs to is owner by the current user
    @PostMapping("/{taskId}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable Long taskId, @PathVariable Long commentId,@AuthenticationPrincipal UserDetails userDetails) {
        taskService.getByIdAndOwner(taskId, userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found or access denied"));
        commentService.delete(commentId);
        return "redirect:/tasks/" + taskId;
    }
}
