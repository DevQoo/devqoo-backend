package com.devqoo.backend.post.service;

import com.devqoo.backend.category.entity.Category;
import com.devqoo.backend.post.dto.form.PostRegisterForm;
import com.devqoo.backend.user.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostFacade {

    private final EntityManager em;
    private final PostService postService;


    @Transactional
    public Long createPost(PostRegisterForm postRegisterForm) {

        log.info("====> Facade createPost in");

        // 유저 검증
        User user = em.getReference(User.class, postRegisterForm.getUserId());

        // 카테고리 검증
        Category category = em.getReference(Category.class, postRegisterForm.getCategoryId());

        return postService.createPost(postRegisterForm, user, category);
    }
}
