package com.devqoo.backend.post.dto.response;

import java.util.List;

public record CursorPageResponse<T>(
    List<T> content,
    Long nextPostId,
    boolean hasNext
) {

    public static <T> CursorPageResponse<T> of(
        List<T> content,
        Long nextPostId,
        boolean hasNext
    ) {
        return new CursorPageResponse<>(content, nextPostId, hasNext);
    }
}
