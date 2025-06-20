package com.devqoo.backend.post.enums;

import com.devqoo.backend.post.entity.QPost;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import java.util.List;
import lombok.Getter;

@Getter
public enum PostSortField {

    // 최신 순
    POST_ID {

        @Override
        public List<OrderSpecifier<?>> getOrderSpecifiers(QPost post) {
            return List.of(new OrderSpecifier<>(Order.DESC, post.postId));
        }
    },

    // 조회수 순
    VIEW_COUNT {

        @Override
        public List<OrderSpecifier<?>> getOrderSpecifiers(QPost post) {
            return List.of (
                new OrderSpecifier<>(Order.DESC, post.viewCount),
                new OrderSpecifier<>(Order.DESC, post.postId)  // 보조 정렬
            );
        }
    };


    public abstract List<OrderSpecifier<?>> getOrderSpecifiers(QPost post);
}
