package com.devqoo.backend.post.repository;

import com.devqoo.backend.post.entity.Post;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    // 게시글 조회 (postId, userId, categoryId 기준)
    @Query("""
        SELECT p
          FROM Post p
         WHERE p.postId = :postId
           AND p.category.categoryId = :categoryId
           AND p.user.userId = :userId""")
    Optional<Post> findPostForUpdate(
        @Param("postId") Long postId,
        @Param("categoryId") Long categoryId,
        @Param("userId") Long userId);
}
