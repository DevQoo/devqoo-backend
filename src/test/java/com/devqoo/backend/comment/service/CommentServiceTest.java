package com.devqoo.backend.comment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

import com.devqoo.backend.category.entity.Category;
import com.devqoo.backend.comment.dto.form.RegisterCommentForm;
import com.devqoo.backend.comment.entity.Comment;
import com.devqoo.backend.comment.repository.CommentRepository;
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
        String postContent = "댓글 내용";
        RegisterCommentForm newCommentForm = new RegisterCommentForm(1L, 1L, postContent);

        User mockUser = EntityProvider.createUser();
        given(userRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(mockUser));

        Category mockCategory = EntityProvider.createCategory("카테고리");
        Post mockPost = EntityProvider.createPost(mockUser, mockCategory);
        given(postRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(mockPost));

        given(commentRepository.save(any(Comment.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        Comment comment = commentService.createComment(newCommentForm);

        // then
        assertThat(comment.getContent()).isEqualTo(postContent);
        assertThat(comment.getPost()).isEqualTo(mockPost);
        assertThat(comment.getAuthor()).isEqualTo(mockUser);
    }
}
