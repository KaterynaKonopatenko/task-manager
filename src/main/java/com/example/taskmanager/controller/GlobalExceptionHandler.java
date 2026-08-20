package com.example.taskmanager.controller;

import com.example.taskmanager.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

// global exception handler catches errors from all controllers
@ControllerAdvice
public class GlobalExceptionHandler {

   // resource not found or access denied (IDOR) protection -> 404
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(ResourceNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    // any other unexpected error -> 500 don't leak internal details to the user
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleOther(Exception ex, Model model) {
        model.addAttribute("errorMessage","Something went wrong on our side. Please try again");
        return "error";
    }
}
