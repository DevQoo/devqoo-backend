package com.devqoo.backend.comment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

import com.devqoo.backend.category.entity.Category;
import com.devqoo.backend.comment.dto.form.RegisterCommentForm;
import com.devqoo.backend.comment.entity.Comment;
import com.devqoo.backend.comment.repository.CommentRepository;
import com.devqoo.backend.common.exception.BusinessException;
import com.devqoo.backend.common.exception.ErrorCode;
import com.devqoo.backend.post.entity.Post;
import com.devqoo.backend.post.repository.PostRepository;
import com.devqoo.backend.provider.EntityProvider;
import com.devqoo.backend.user.entity.User;
import com.devqoo.backend.user.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    public static final String POST_CONTENT = "댓글 내용";
    public static final RegisterCommentForm NEW_COMMENT_FORM = new RegisterCommentForm(1L, 1L, POST_CONTENT);

    @InjectMocks
    private CommentService commentService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentRepository commentRepository;

    @DisplayName("댓글을 DB에 저장 성공한다")
    @Test
    void generate_comment_when_save_then_success() {
        // given
        User mockUser = EntityProvider.createUser();
        given(userRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(mockUser));

        Category mockCategory = EntityProvider.createCategory("카테고리");
        Post mockPost = EntityProvider.createPost(mockUser, mockCategory);
        given(postRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(mockPost));

        given(commentRepository.save(any(Comment.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        Comment comment = commentService.createComment(NEW_COMMENT_FORM);

        // then
        assertThat(comment.getContent()).isEqualTo(POST_CONTENT);
        assertThat(comment.getPost()).isEqualTo(mockPost);
        assertThat(comment.getAuthor()).isEqualTo(mockUser);
    }

    @DisplayName("댓글 생성 시, 작성자 정보가 없으면 예외를 던진다")
    @Test
    void given_not_exists_user_when_save_then_throw_BusinessException() {
        // given
        given(userRepository.findById(any(Long.class)))
            .willThrow(new BusinessException(ErrorCode.USER_NOT_FOUND));

        // when && then
        assertThatThrownBy(() -> commentService.createComment(NEW_COMMENT_FORM))
            .isInstanceOf(BusinessException.class)
            .hasMessage(ErrorCode.USER_NOT_FOUND.getMessage());
    }

    @DisplayName("댓글 생성 시, 게시글 정보가 없으면 예외를 던진다")
    @Test
    void given_not_exists_post_when_save_then_throw_BusinessException() {
        // given
        User mockUser = EntityProvider.createUser();
        given(userRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(mockUser));

        given(postRepository.findById(any(Long.class)))
            .willThrow(new BusinessException(ErrorCode.POST_NOT_FOUND));

        // when && then
        assertThatThrownBy(() -> commentService.createComment(NEW_COMMENT_FORM))
            .isInstanceOf(BusinessException.class)
            .hasMessage(ErrorCode.POST_NOT_FOUND.getMessage());
    }
}
