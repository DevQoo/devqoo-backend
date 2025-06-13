package com.devqoo.backend.comment.dto.response;

import com.devqoo.backend.comment.entity.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record CommentResponseDto(
    Long commentId,
    String content,
    Long userId,
    String authorName,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime createdAt
) {

    public static CommentResponseDto from(Comment comment) {
        return new CommentResponseDto(
            comment.getCommentId(),
            comment.getContent(),
            comment.getAuthor().getUserId(),
            comment.getAuthor().getNickname(),
            comment.getCreatedAt()
        );
    }
}
