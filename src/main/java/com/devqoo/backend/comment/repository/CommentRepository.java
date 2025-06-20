package com.devqoo.backend.comment.repository;

import com.devqoo.backend.comment.entity.Comment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long>, CustomQueryCommentRepository {

    @Query("SELECT c FROM Comment c join fetch c.author join fetch c.post WHERE c.commentId = :commentId")
    Optional<Comment> findWithAuthorAndPostById(Long commentId);

    // postIds에 대한 댓글 수 조회
    @Query("""
        SELECT c.post.postId, COUNT(c)
          FROM Comment c
         WHERE c.post.postId IN :postIds
      GROUP BY c.post.postId
    """)
    List<Object[]> countCommentsByPostIds(List<Long> postIds);
}
