package com.devqoo.backend.post.repository;

import com.devqoo.backend.post.entity.Post;
import com.devqoo.backend.post.entity.QPost;
import com.devqoo.backend.post.enums.PostSortField;
import com.devqoo.backend.post.enums.SortDirection;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public List<Post> searchPostsByCursor(String keyword, String searchType, PostSortField sortField,
        SortDirection direction, Long lastPostId, int lastViewCount, int size) {

        QPost post = QPost.post;
        BooleanBuilder condition = new BooleanBuilder();

        // 검색 조건
        if (keyword != null && !keyword.isBlank()) {
            String type = (searchType == null || searchType.isBlank()) ? "title_content" : searchType;

            log.debug("==========> type: {} and keywork: {}", type, keyword);
            switch (type) {
                case "title" -> condition.and(post.title.containsIgnoreCase(keyword));
                case "content" -> condition.and(post.content.containsIgnoreCase(keyword));
                case "titleContent" -> condition.and(
                    post.title.containsIgnoreCase(keyword)
                        .or(post.content.containsIgnoreCase(keyword))
                );
            }
        }

        // 커서 조건
        // 최신 순
        if (sortField == PostSortField.POST_ID && lastPostId != null) {
            condition.and(direction == SortDirection.DESC ?
                post.postId.lt(lastPostId)
                : post.postId.gt(lastPostId));
        }

        // 조회수 순
        if (sortField == PostSortField.VIEW_COUNT && lastViewCount >= 0 && lastPostId != null) {
            BooleanExpression cursorCondition = direction == SortDirection.DESC ?
                post.viewCount.lt(lastViewCount)
                    .or(post.viewCount.eq(lastViewCount).and(post.postId.lt(lastPostId)))
                : post.viewCount.gt(lastViewCount)
                    .or(post.viewCount.eq(lastViewCount).and(post.postId.gt(lastPostId)));

            condition.and(cursorCondition);
        }

        // 정렬 필드 + 보조 정렬 (postId)
        OrderSpecifier<?> primary =
            switch (sortField) {
                case POST_ID -> direction == SortDirection.DESC ? post.postId.desc() : post.postId.asc();
                case VIEW_COUNT -> direction == SortDirection.DESC ? post.viewCount.desc() : post.viewCount.asc();
            };

        List<OrderSpecifier<?>> orders = new ArrayList<>();
        orders.add(primary);

        // 보조 정렬은 필요할 때만 추가 (중복 제거)
        if (sortField == PostSortField.VIEW_COUNT) {
            orders.add(post.postId.desc()); // 항상 최신순 보조 정렬
        }

        return jpaQueryFactory
            .selectFrom(post)
            .where(condition)
            .orderBy(orders.toArray(new OrderSpecifier[0]))
            .limit(size)
            .fetch();
    }
}
