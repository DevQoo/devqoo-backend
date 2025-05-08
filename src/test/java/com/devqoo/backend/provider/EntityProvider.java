package com.devqoo.backend.provider;

import com.devqoo.backend.category.entity.Category;

public abstract class EntityProvider {

    public static Category createCategory(String name) {
        return new Category(name);
    }
}
