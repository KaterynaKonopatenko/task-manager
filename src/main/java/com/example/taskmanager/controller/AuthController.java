package com.example.taskmanager.controller;


import com.example.taskmanager.dto.RegisterForm;
import com.example.taskmanager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        // pass empty form object to the template
        model.addAttribute("registerForm", new RegisterForm());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registerForm") RegisterForm form, BindingResult result, Model model) {
        // if validation failed show errors and stay on register page
        if (result.hasErrors()) {
            return "register";
        }
        try {
            userService.register(form.getUsername(), form.getEmail(), form.getPassword());
            return "redirect:/login";
        }
        // cath only the duplicate-username/email case(DB unique constraint violation), not every posible exeption
        catch (DataIntegrityViolationException e) {
            model.addAttribute("error", "Username or email already exists");
            return "register";
        }
    }
}
