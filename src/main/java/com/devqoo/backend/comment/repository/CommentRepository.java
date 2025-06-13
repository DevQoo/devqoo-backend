package com.devqoo.backend.comment.repository;

import com.devqoo.backend.comment.entity.Comment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long>, CustomQueryCommentRepository {

    @Query("SELECT c FROM Comment c join fetch c.author join fetch c.post WHERE c.commentId = :commentId")
    Optional<Comment> findWithAuthorAndPostById(Long commentId);
}
