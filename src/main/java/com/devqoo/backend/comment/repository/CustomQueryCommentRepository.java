package com.devqoo.backend.comment.repository;

import com.devqoo.backend.comment.entity.Comment;
import java.util.List;

public interface CustomQueryCommentRepository {

    List<Comment> findCommentsByCursor(Long postId, Long cursorId, int size);

}
