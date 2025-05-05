package com.devqoo.backend.post.service;

import com.devqoo.backend.category.entity.Category;
import com.devqoo.backend.post.dto.form.PostRegisterForm;
import com.devqoo.backend.post.entity.Post;
import com.devqoo.backend.post.repository.PostRepository;
import com.devqoo.backend.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;


    /* post create */
    @Transactional
    public Long createPost(PostRegisterForm postRegisterForm, User user, Category category) {
        Post post = Post.builder()
            .category(category)
            .user(user)
            .title(postRegisterForm.getTitle())
            .content(postRegisterForm.getContent())
            .build();

        // 저장
        Post savePost = postRepository.save(post);
        return savePost.getPostId();
    }
}
