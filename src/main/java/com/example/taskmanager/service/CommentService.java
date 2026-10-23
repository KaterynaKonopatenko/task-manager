package com.example.taskmanager.service;

import com.example.taskmanager.model.Comment;
import com.example.taskmanager.model.User;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.repo.CommentRepository;
import com.example.taskmanager.repo.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.taskmanager.exception.ResourceNotFoundException;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;

    public CommentService(CommentRepository commentRepository, TaskRepository taskRepository) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
    }

    // add a new comment but chek right here in the service that the task really belongs to this user
    @Transactional
    public Comment add(String text, Long taskId, User user) {
        Task task = taskRepository.findByIdAndProjectOwnerUsername(taskId, user.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        Comment comment = new Comment();
        comment.setText(text);
        comment.setTask(task);
        comment.setUser(user);
        return commentRepository.save(comment);
    }

    // get all comments for a task but only if the task belongs to this user
    @Transactional(readOnly = true)
    public List<Comment> getByTask(Long taskId, String username) {
        taskRepository.findByIdAndProjectOwnerUsername(taskId, username)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        return commentRepository.findByTaskId(taskId);
    }

    // delete a comment, but only if it really belongs to this task
    // this prevents deleting a comment from someone else's task by guessing its id
    @Transactional
    public void delete(Long id, Long taskId) {
        Comment comment = commentRepository.findByIdAndTaskId(id,taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        commentRepository.delete(comment);
    }
}
