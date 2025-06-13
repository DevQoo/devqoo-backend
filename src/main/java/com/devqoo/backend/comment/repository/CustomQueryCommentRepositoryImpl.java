package com.devqoo.backend.comment.repository;

import com.devqoo.backend.comment.entity.Comment;
import com.devqoo.backend.comment.entity.QComment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomQueryCommentRepositoryImpl implements CustomQueryCommentRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Comment> findCommentsByCursor(Long postId, Long cursorId, int size) {
        QComment comment = QComment.comment;
        return queryFactory
            .selectFrom(comment)
            .join(comment.author).fetchJoin()
            .where(
                comment.post.postId.eq(postId),
                cursorId != null ? comment.commentId.gt(cursorId) : null)
            .orderBy(comment.commentId.asc())
            .limit(size + 1)
            .fetch();
    }
}
