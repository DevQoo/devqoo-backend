package com.devqoo.backend.post.repository;

import com.devqoo.backend.post.entity.Post;
import com.devqoo.backend.post.enums.PostSortField;
import com.devqoo.backend.post.enums.SortDirection;
import java.util.List;

public interface PostRepositoryCustom {

    List<Post> searchPostsByCursor(
        String keyword,
        String searchType,
        PostSortField sortField,
        SortDirection direction,
        Long lastPostId,
        int lastViewCount,
        int size
    );
}
