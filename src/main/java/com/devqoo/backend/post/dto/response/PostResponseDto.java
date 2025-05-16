package com.devqoo.backend.post.dto.response;

import com.devqoo.backend.category.dto.response.CategoryResponseDto;
import com.devqoo.backend.post.entity.Post;
import com.devqoo.backend.user.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record PostResponseDto(
    Long postId,
    CategoryResponseDto categoryInfo,
    getUserInfo userInfo,
    String title,
    String content,
    int viewCount,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime createdAt
) {

    // PostResponseDto 생성
    public static PostResponseDto from(Post post) {
        return new PostResponseDto(
            post.getPostId(),
            getCategoryInfo(post),
            getUserInfo.from(post.getUser()),
            post.getTitle(),
            post.getContent(),
            post.getViewCount(),
            post.getCreatedAt()
        );
    }

    // CategoryResponseDto
    private static CategoryResponseDto getCategoryInfo(Post post) {
        return new CategoryResponseDto(post.getCategory());
    }

    // User 응답 DTO
    // TODO: UserResponseDto로 변환해야함
    public record getUserInfo(Long userId, String nickname) {
        public static getUserInfo from(User user) {
            return new getUserInfo(user.getUserId(), user.getNickname());
        }
    }
}
