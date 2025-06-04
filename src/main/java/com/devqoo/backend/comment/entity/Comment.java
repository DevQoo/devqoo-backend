package com.devqoo.backend.comment.entity;

import com.devqoo.backend.common.entity.BaseTimeEntity;
import com.devqoo.backend.post.entity.Post;
import com.devqoo.backend.user.entity.User;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false,
        foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,
        foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User author;

    @Column(name = "content", nullable = false, length = 500)
    private String content;

    @Builder
    public Comment(Post post, User author, String content) {
        this.post = post;
        this.author = author;
        this.content = content;
    }

    public void validateAuthor(User user) {
        if (!Objects.equals(this.author.getUserId(), user.getUserId())) {
            throw new IllegalArgumentException();
        }
    }

    public void validatePost(Post post) {
        if (!Objects.equals(this.post.getPostId(), post.getPostId())) {
            throw new IllegalArgumentException();
        }
    }

    public void updateContent(String content) {
        if (StringUtils.isNotBlank(content)) {
            this.content = content;
        }
    }
}
