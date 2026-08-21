package com.example.taskmanager.dto;

import com.example.taskmanager.model.Comment;

public class CommentDto {
    private final Long id;
    private final String text;
    private final String username;

    public CommentDto(Long id, String text, String username) {
        this.id = id;
        this.text = text;
        this.username = username;
    }

    public static CommentDto from(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText(), comment.getUser().getUsername());
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getUsername() {
        return username;
    }

}
