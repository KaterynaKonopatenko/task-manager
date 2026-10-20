package com.example.taskmanager.repo;

import com.example.taskmanager.model.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // eagerly load the comment author since task.html shows their username
    @EntityGraph(attributePaths = "user")
    List<Comment> findByTaskId(Long taskId);

    // check that a comment really belongs to this task before we delete it
    Optional<Comment> findByIdAndTaskId(Long id, Long taskId);
}
