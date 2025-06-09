package com.devqoo.backend.comment.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;

public record CommentCursorResult(
    List<CommentResponseDto> comments,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime nextCursor,
    boolean hasNext
) {

    public static CommentCursorResult from(List<CommentResponseDto> comments, int size) {
        boolean hasNext = comments.size() > size;
        List<CommentResponseDto> result = hasNext ? comments.subList(0, size) : comments;
        LocalDateTime nextCursor = result.isEmpty() ? null : result.get(result.size() - 1).createdAt();

        return new CommentCursorResult(
            result,
            nextCursor,
            hasNext
        );
    }
}
