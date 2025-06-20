package com.devqoo.backend.post.repository;

import com.devqoo.backend.post.entity.Post;
import com.devqoo.backend.post.entity.QPost;
import com.devqoo.backend.post.enums.PostSortField;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private static final QPost post = QPost.post;


    @Override
    public List<Post> searchPostsByCursor(String keyword, String searchType, PostSortField sortField,
        Long lastPostId, int size) {

        BooleanBuilder condition = new BooleanBuilder()
            .and(buildSearchCondition(keyword, searchType))     // 검색 조건
            .and(buildCursorCondition(lastPostId));             // 커서 조건

        return jpaQueryFactory
            .selectFrom(post)
            .where(condition)
            .orderBy(sortField.getOrderSpecifiers(post)
                .toArray(new OrderSpecifier[0]))
            .limit(size)
            .fetch();
    }

    // 검색 조건
    private BooleanExpression buildSearchCondition(String keyword, String searchType) {

        if (keyword == null || keyword.isBlank()) {
            return null;
        }

        String type = (searchType == null || searchType.isBlank()) ? "title" : searchType;

        log.debug("==========> type: {} and keywork: {}", type, keyword);
        return switch (type) {
            case "title" -> post.title.containsIgnoreCase(keyword);
            case "content" -> post.content.containsIgnoreCase(keyword);
            case "title_content" -> post.title.containsIgnoreCase(keyword)
                .or(post.content.containsIgnoreCase(keyword));
            default -> null;
        };
    }

    // 커서 조건 (항상 postId 기준 -> 최신순)
    private BooleanExpression buildCursorCondition(Long lastPostId) {
        return lastPostId != null ? post.postId.lt(lastPostId) : null;
    }
}

