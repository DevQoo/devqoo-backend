package com.devqoo.backend.comment.service;

import com.devqoo.backend.comment.dto.form.RegisterCommentForm;
import com.devqoo.backend.comment.dto.response.CommentCursorResult;
import com.devqoo.backend.comment.dto.response.CommentResponseDto;
import com.devqoo.backend.comment.entity.Comment;
import com.devqoo.backend.comment.repository.CommentRepository;
import com.devqoo.backend.common.exception.BusinessException;
import com.devqoo.backend.common.exception.ErrorCode;
import com.devqoo.backend.post.entity.Post;
import com.devqoo.backend.post.repository.PostRepository;
import com.devqoo.backend.user.entity.User;
import com.devqoo.backend.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    // 댓글 목록 조회
    @Transactional(readOnly = true)
    public CommentCursorResult getComment(Long postId, LocalDateTime after, int size) {
        LocalDateTime cursor = (after != null) ? after : LocalDateTime.of(1970, 1, 1, 0, 0);
        List<CommentResponseDto> comments = commentRepository.findCommentsByCursor(postId, cursor, size).stream()
            .map(CommentResponseDto::from)
            .toList();
        return CommentCursorResult.from(comments, size);
    }

    // 댓글 생성
    @Transactional
    public Comment createComment(RegisterCommentForm form) {
        // entity 생성 필요 : POST USER CONTENT
        User foundUser = userRepository.findById(form.authorId())
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Post foundPost = postRepository.findById(form.postId())
            .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        Comment comment = new Comment(foundPost, foundUser, form.content());
        return commentRepository.save(comment);
    }

    // 댓글 수정
    @Transactional
    public void updateComment(Long commentId, RegisterCommentForm form) {
        Comment foundComment = commentRepository.findWithAuthorAndPostById(commentId)
            .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        User user = userRepository.findById(form.authorId())
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Post post = postRepository.findById(form.postId())
            .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        foundComment.validateAuthor(user);
        foundComment.validatePost(post);
        foundComment.updateContent(form.content());
    }

    // 댓글 삭제
}
