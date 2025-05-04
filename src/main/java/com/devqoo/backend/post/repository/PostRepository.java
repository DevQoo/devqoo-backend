package com.devqoo.backend.post.repository;

import com.devqoo.backend.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}
