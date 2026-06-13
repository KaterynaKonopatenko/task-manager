package com.example.taskmanager.service;

import com.example.taskmanager.model.Comment;
import com.example.taskmanager.model.User;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.repo.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment add(String text, Task task, User user) {
        Comment comment = new Comment();
        comment.setText(text);
        comment.setTask(task);
        comment.setUser(user);
        return commentRepository.save(comment);
    }

    public List<Comment> getByTask(Long taskId) {
        return commentRepository.findByTaskId(taskId);
    }

    public void delete(Long id) {
        commentRepository.deleteById(id);
    }
}
