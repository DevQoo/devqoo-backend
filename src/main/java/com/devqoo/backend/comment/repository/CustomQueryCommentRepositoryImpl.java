package com.devqoo.backend.comment.repository;

import com.devqoo.backend.comment.entity.Comment;
import com.devqoo.backend.comment.entity.QComment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomQueryCommentRepositoryImpl implements CustomQueryCommentRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Comment> findCommentsByCursor(Long postId, LocalDateTime after, int size) {
        QComment comment = QComment.comment;
        return queryFactory
            .selectFrom(comment)
            .join(comment.author).fetchJoin()
            .where(
                comment.post.postId.eq(postId),
                comment.createdAt.gt(after)
            )
            .orderBy(comment.createdAt.asc())
            .limit(size + 1)
            .fetch();
    }
}
