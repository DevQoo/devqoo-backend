package com.devqoo.backend.post.service;

import com.devqoo.backend.category.entity.Category;
import com.devqoo.backend.category.service.CategoryService;
import com.devqoo.backend.post.dto.form.PostUpdateForm;
import com.devqoo.backend.post.dto.response.PostResponseDto;
import com.devqoo.backend.post.dto.form.PostRegisterForm;
import com.devqoo.backend.post.entity.Post;
import com.devqoo.backend.user.entity.User;
import com.devqoo.backend.user.service.UserService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostFacade {

    private final PostService postService;
    private final CategoryService categoryService;
    private final UserService userService;


    /*
    * userId, categoryId 검증 후 게시글 생성
    * */
    public Long createPost(PostRegisterForm postRegisterForm) {
        log.debug("====> Facade createPost in");

        // 유저 검증
        User user = userService.findById(postRegisterForm.getUserId());

        // 카테고리 검증
        Category category = categoryService.findById(postRegisterForm.getCategoryId());

        return postService.createPost(postRegisterForm, user, category);
    }

    // 게시글 수정
    public Long updatePost(Long postId, PostUpdateForm postUpdateForm) {
        log.debug("====> Facade updatePost in");
        return postService.updatePost(postId, postUpdateForm);
    }

    // 게시글 상세 조회
    public PostResponseDto getPostDetail(Long postId) {
        log.debug("====> Facade getPostDetail in");
        return postService.getPostDetailById(postId);
    }

}
