package com.example.taskmanager.repo;

import com.example.taskmanager.model.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // eagerly load the comment author since task.html shows their username
    @EntityGraph(attributePaths = "user")
    List<Comment> findByTaskId(Long taskId);
}
