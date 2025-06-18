package com.devqoo.backend.post.service;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.given;

import com.devqoo.backend.category.entity.Category;
import com.devqoo.backend.category.repository.CategoryRepository;
import com.devqoo.backend.comment.repository.CommentRepository;
import com.devqoo.backend.common.exception.BusinessException;
import com.devqoo.backend.common.exception.ErrorCode;
import com.devqoo.backend.post.dto.form.PostForm;
import com.devqoo.backend.post.dto.response.CursorPageResponse;
import com.devqoo.backend.post.dto.response.PostResponseDto;
import com.devqoo.backend.post.entity.Post;
import com.devqoo.backend.post.enums.PostSortField;
import com.devqoo.backend.post.repository.PostRepository;
import com.devqoo.backend.provider.EntityProvider;
import com.devqoo.backend.provider.PostFixture;
import com.devqoo.backend.provider.UserFixture;
import com.devqoo.backend.user.entity.User;
import com.devqoo.backend.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private PostService postService;

    private final Category category = EntityProvider.createCategory("category");
    private final User user = UserFixture.createUser();


    @Test
    @DisplayName("createPost: 게시글 생성")
    void createPost_success() {
        // given
        PostForm form = new PostForm(1L, 1L, "title", "content");
        Post saved = PostFixture.createPost(1L, user, category, "title", "content");
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(category));
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(postRepository.save(any(Post.class))).willReturn(saved);

        // when
        Long postId = postService.createPost(form);

        // then
        then(postId).isEqualTo(saved.getPostId());
    }

    @Test
    @DisplayName("createPost: 카테고리가 없으면 예외")
    void createPost_categoryNotFound() {
        // given
        PostForm form = new PostForm(1L, 1L, "title", "content");
        given(categoryRepository.findById(anyLong())).willReturn(Optional.empty());

        // when && then
        thenThrownBy(() -> postService.createPost(form))
            .isInstanceOf(BusinessException.class)
            .hasMessage(ErrorCode.CATEGORY_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("createPost: 작성자가 없으면 예외")
    void createPost_userNotFound() {
        // given
        PostForm form = new PostForm(1L, 1L, "title", "content");
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(category));
        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        // when && then
        thenThrownBy(() -> postService.createPost(form))
            .isInstanceOf(BusinessException.class)
            .hasMessage(ErrorCode.USER_NOT_FOUND.getMessage());
    }

    @Nested
    class UpdatePost {

        @Test
        @DisplayName("updatePost: 게시글 수정")
        void updatePost_success() {
            // given
            Long postId = 1L;
            Post post = PostFixture.createPost(postId, user, category, "old", "old");
            PostForm form = new PostForm(1L, 1L, "newTitle", "newContent");
            given(userRepository.existsById(anyLong())).willReturn(true);
            given(categoryRepository.existsById(anyLong())).willReturn(true);
            given(postRepository.findPostForUpdate(anyLong(), anyLong(), anyLong()))
                .willReturn(Optional.of(post));

            // when
            Long result = postService.updatePost(postId, form);

            // then
            then(result).isEqualTo(postId);
            then(post.getTitle()).isEqualTo("newTitle");
            then(post.getContent()).isEqualTo("newContent");
        }

        @Test
        @DisplayName("updatePost: 작성자가 없으면 예외")
        void updatePost_userNotFound() {
            // given
            PostForm form = new PostForm(1L, 1L, "t", "c");
            given(userRepository.existsById(anyLong())).willReturn(false);

            // when && then
            thenThrownBy(() -> postService.updatePost(1L, form))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.USER_NOT_FOUND.getMessage());
        }

        @Test
        @DisplayName("updatePost: 카테고리가 없으면 예외")
        void updatePost_categoryNotFound() {
            // given
            PostForm form = new PostForm(1L, 1L, "t", "c");
            given(userRepository.existsById(anyLong())).willReturn(true);
            given(categoryRepository.existsById(anyLong())).willReturn(false);

            // when && then
            thenThrownBy(() -> postService.updatePost(1L, form))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.CATEGORY_NOT_FOUND.getMessage());
        }

        @Test
        @DisplayName("updatePost: 게시글이 없으면 예외")
        void updatePost_postNotFound() {
            // given
            PostForm form = new PostForm(1L, 1L, "t", "c");
            given(userRepository.existsById(anyLong())).willReturn(true);
            given(categoryRepository.existsById(anyLong())).willReturn(true);
            given(postRepository.findPostForUpdate(anyLong(), anyLong(), anyLong()))
                .willReturn(Optional.empty());

            // when && then
            thenThrownBy(() -> postService.updatePost(1L, form))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.POST_NOT_FOUND.getMessage());
        }
    }

    @Test
    @DisplayName("deletePost: 게시글 삭제")
    void deletePost_success() {
        // given
        Post post = PostFixture.createPost();
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));

        // when
        postService.deletePost(1L);

        // then
        org.mockito.BDDMockito.then(postRepository).should().delete(post);
    }

    @Test
    @DisplayName("deletePost: 게시글이 없으면 예외")
    void deletePost_notFound() {
        // given
        given(postRepository.findById(anyLong())).willReturn(Optional.empty());

        // when && then
        thenThrownBy(() -> postService.deletePost(1L))
            .isInstanceOf(BusinessException.class)
            .hasMessage(ErrorCode.POST_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("getPostDetail: 게시글 상세 조회")
    void getPostDetail_success() {
        // given
        Post post = PostFixture.createPost();
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));

        // when
        PostResponseDto result = postService.getPostDetail(1L);

        // then
        then(result.postId()).isEqualTo(post.getPostId());
        then(result.title()).isEqualTo(post.getTitle());
    }

    @Test
    @DisplayName("getPostDetail: 게시글이 없으면 예외")
    void getPostDetail_notFound() {
        // given
        given(postRepository.findById(anyLong())).willReturn(Optional.empty());

        // when && then
        thenThrownBy(() -> postService.getPostDetail(1L))
            .isInstanceOf(BusinessException.class)
            .hasMessage(ErrorCode.POST_NOT_FOUND.getMessage());
    }

    @Nested
    class GetPostsByCursor {

        @Test
        @DisplayName("게시글 목록 조회 - 다음 페이지 있음")
        void getPosts_hasNext() {
            // given
            Post post1 = PostFixture.createPost(1L);
            Post post2 = PostFixture.createPost(2L);
            Post post3 = PostFixture.createPost(3L);

            given(postRepository.searchPostsByCursor(
                eq("제목"), // keyword
                eq("title"), // searchType
                eq(PostSortField.POST_ID), // 정렬 필드
                eq(2L),   // lastPostId
                eq(3)     // size
            )).willReturn(new ArrayList<>(List.of(post1, post2, post3)));

            // 댓글 수 mock
            given(commentRepository.countCommentsByPostIds(List.of(1L, 2L)))
                .willReturn(List.of(
                    new Object[]{1L, 2L},
                    new Object[]{2L, 0L}
                ));

            // when
            CursorPageResponse<PostResponseDto> page = postService.getPostsByCursor(
                "제목", "title", PostSortField.POST_ID, 2L, 2);


            // then
            then(page.hasNext()).isTrue();
            then(page.nextPostId()).isEqualTo(post2.getPostId());
            then(page.content()).hasSize(2);

            // then 댓글 수
            PostResponseDto dto1 = page.content().get(0);
            PostResponseDto dto2 = page.content().get(1);

            then(dto1.postId()).isEqualTo(1L);
            then(dto1.commentCount()).isEqualTo(2L);

            then(dto2.postId()).isEqualTo(2L);
            then(dto2.commentCount()).isEqualTo(0L);
        }

        @Test
        @DisplayName("게시글 목록 조회 - 다음 페이지 없음")
        void getPosts_noNext() {
            // given
            Post post1 = PostFixture.createPost(1L);
            given(postRepository.searchPostsByCursor(
                isNull(),
                isNull(),
                eq(PostSortField.POST_ID),
                isNull(),
                eq(2)
            )).willReturn(new ArrayList<>(List.of(post1)));

            List<Object[]> commentCounts = new ArrayList<>();
            commentCounts.add(new Object[]{1L, 3L});

            given(commentRepository.countCommentsByPostIds(anyList()))
                .willReturn(commentCounts);

            // when
            CursorPageResponse<PostResponseDto> page = postService.getPostsByCursor(
                null, null, PostSortField.POST_ID, null, 1);

            // then
            then(page.hasNext()).isFalse();
            then(page.nextPostId()).isEqualTo(post1.getPostId());
            then(page.content()).hasSize(1);

            // then 댓글 수
            PostResponseDto dto = page.content().get(0);
            then(dto.postId()).isEqualTo(1L);
            then(dto.commentCount()).isEqualTo(3L);
        }
    }
}
