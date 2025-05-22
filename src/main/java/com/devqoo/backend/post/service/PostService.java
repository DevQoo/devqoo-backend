package com.devqoo.backend.post.service;

import static com.devqoo.backend.common.exception.ErrorCode.CATEGORY_NOT_FOUND;
import static com.devqoo.backend.common.exception.ErrorCode.POST_NOT_FOUND;
import static com.devqoo.backend.common.exception.ErrorCode.USER_NOT_FOUND;

import com.devqoo.backend.category.entity.Category;
import com.devqoo.backend.category.repository.CategoryRepository;
import com.devqoo.backend.common.exception.BusinessException;
import com.devqoo.backend.post.dto.form.PostForm;
import com.devqoo.backend.post.dto.response.PostResponseDto;
import com.devqoo.backend.post.entity.Post;
import com.devqoo.backend.post.repository.PostRepository;
import com.devqoo.backend.user.entity.User;
import com.devqoo.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;


    // 게시글 생성
    @Transactional
    public Long createPost(PostForm postForm) {
        log.debug("====> postService createPost in");

        // 카테고리
        Category category = categoryRepository.findById(postForm.categoryId())
            .orElseThrow(() -> new BusinessException(CATEGORY_NOT_FOUND));

        // 유저
        User user = userRepository.findById(postForm.userId())
            .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        Post post = Post.builder()
            .category(category)
            .user(user)
            .title(postForm.title())
            .content(postForm.content())
            .build();

        return postRepository.save(post).getPostId();
    }

    // 게시글 수정
    @Transactional
    public Long updatePost(Long postId, PostForm postForm) {
        log.debug("====> postService updatePost in");

        // userId, categoryId 유효성 체크
        if (!userRepository.existsById(postForm.userId())) {
            throw new BusinessException(USER_NOT_FOUND);
        }

        if (!categoryRepository.existsById(postForm.categoryId())) {
            throw new BusinessException(CATEGORY_NOT_FOUND);
        }

        Post post = postRepository.findPostForUpdate(postId, postForm.categoryId(), postForm.userId())
            .orElseThrow(() -> new BusinessException(POST_NOT_FOUND));

        // 제목 수정
        if (postForm.title() != null && !postForm.title().isBlank()) {
            post.updateTitle(postForm.title());
        }

        // 내용 수정
        if (postForm.content() != null && !postForm.content().isBlank()) {
            post.updateContent(postForm.content());
        }

        return post.getPostId();
    }

    // 게시글 상세 조회
    @Transactional(readOnly = true)
    public PostResponseDto getPostDetail(Long postId) {
        log.debug("====> postService getPostDetailById in");

        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new BusinessException(POST_NOT_FOUND));

        return PostResponseDto.from(post);
    }

}
