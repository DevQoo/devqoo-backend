package com.devqoo.backend.comment.dto.response;

import java.util.List;

public record CommentCursorResult(
    List<CommentResponseDto> comments,
    Long nextCursor,
    boolean hasNext
) {

    public static CommentCursorResult from(List<CommentResponseDto> comments, int size) {
        boolean hasNext = comments.size() > size;
        List<CommentResponseDto> result = hasNext ? comments.subList(0, size) : comments;
        Long nextCursor = result.isEmpty() ? null : result.get(result.size() - 1).commentId();

        return new CommentCursorResult(
            result,
            nextCursor,
            hasNext
        );
    }
}
