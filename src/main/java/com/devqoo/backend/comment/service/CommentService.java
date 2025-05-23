package com.devqoo.backend.comment.service;

import com.devqoo.backend.comment.dto.form.RegisterCommentForm;
import com.devqoo.backend.comment.entity.Comment;
import com.devqoo.backend.comment.repository.CommentRepository;
import com.devqoo.backend.common.exception.BusinessException;
import com.devqoo.backend.common.exception.ErrorCode;
import com.devqoo.backend.post.entity.Post;
import com.devqoo.backend.post.repository.PostRepository;
import com.devqoo.backend.user.entity.User;
import com.devqoo.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    // 댓글 목록 조회

    // 댓글 생성
    public Comment createComment(RegisterCommentForm form) {
        // entity 생성 필요 : POST USER CONTENT
        Long authorId = form.authorId();
        User foundUser = userRepository.findById(authorId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Long postId = form.postId();
        Post foundPost = postRepository.findById(postId)
            .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        String content = form.content();
        Comment comment = new Comment(foundPost, foundUser, content);
        return commentRepository.save(comment);
    }

    // 댓글 수정

    // 댓글 삭제
}
