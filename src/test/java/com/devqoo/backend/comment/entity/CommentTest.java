package com.devqoo.backend.comment.entity;

import static org.assertj.core.api.BDDAssertions.thenThrownBy;

import com.devqoo.backend.post.entity.Post;
import com.devqoo.backend.provider.EntityProvider;
import com.devqoo.backend.provider.PostFixture;
import com.devqoo.backend.provider.UserFixture;
import com.devqoo.backend.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CommentTest {

    @DisplayName("댓글 작성자 불일치")
    @Test
    void isNotEquals_same_user() {
        User user = UserFixture.createUser(1L);
        User antotherUser = UserFixture.createUser(2L);
        Post post = PostFixture.createPost();
        Comment comment = EntityProvider.createComment(post, user, "댓글 내용");

        thenThrownBy(() -> comment.validateAuthor(antotherUser))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Post 불일치")
    @Test
    void not_equals_post() {
        // given
        User user = UserFixture.createUser();
        Post post = PostFixture.createPost(1L);
        Post anotherPost = PostFixture.createPost(2L);
        Comment comment = EntityProvider.createComment(post, user, "댓글 내용");

        thenThrownBy(() -> comment.validatePost(anotherPost))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
