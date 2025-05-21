package com.devqoo.backend.post.service;

import static com.devqoo.backend.common.exception.ErrorCode.POST_NOT_FOUND;

import com.devqoo.backend.category.entity.Category;
import com.devqoo.backend.common.exception.BusinessException;
import com.devqoo.backend.post.dto.form.PostRegisterForm;
import com.devqoo.backend.post.dto.form.PostUpdateForm;
import com.devqoo.backend.post.dto.response.PostResponseDto;
import com.devqoo.backend.post.entity.Post;
import com.devqoo.backend.post.repository.PostRepository;
import com.devqoo.backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;


    // post 생성
    @Transactional
    public Long createPost(PostRegisterForm postRegisterForm, User user, Category category) {
        log.debug("====> postService createPost in");

        Post post = Post.builder()
            .category(category)
            .user(user)
            .title(postRegisterForm.getTitle())
            .content(postRegisterForm.getContent())
            .build();

        return postRepository.save(post).getPostId();
    }

    // post 수정
    @Transactional
    public Long updatePost(Long postId, PostUpdateForm postUpdateForm) {
        log.debug("====> postService updatePost in");

        Post post = this.findById(postId);

        // 제목 수정
        if (postUpdateForm.getTitle() != null && !postUpdateForm.getTitle().isBlank()) {
            post.updateTitle(postUpdateForm.getTitle());
        }

        // 내용 수정
        if (postUpdateForm.getContent() != null && !postUpdateForm.getContent().isBlank()) {
            post.updateContent(postUpdateForm.getContent());
        }

        return post.getPostId();
    }

    // 게시글 엔티티를 DTO로 변환
    @Transactional(readOnly = true)
    public PostResponseDto getPostDetailById(Long postId) {
        log.debug("====> postService getPostDetailById in");

        Post post = findById(postId);
        return PostResponseDto.from(post);
    }

    /*
     * 조회 (postId 기준)
     * */
    @Transactional(readOnly = true)
    public Post findById(Long postId) {
        return postRepository.findById(postId)
            .orElseThrow(() -> new BusinessException(POST_NOT_FOUND));
    }

}
