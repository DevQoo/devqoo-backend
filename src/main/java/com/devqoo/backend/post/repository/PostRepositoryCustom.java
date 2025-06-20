package com.devqoo.backend.post.repository;

import com.devqoo.backend.post.entity.Post;
import com.devqoo.backend.post.enums.PostSortField;
import java.util.List;

public interface PostRepositoryCustom {

    List<Post> searchPostsByCursor(
        String keyword,
        String searchType,
        PostSortField sortField,
        Long lastPostId,
        int size
    );
}
