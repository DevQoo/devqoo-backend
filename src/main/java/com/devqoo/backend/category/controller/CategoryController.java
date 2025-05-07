package com.devqoo.backend.category.controller;

import com.devqoo.backend.category.dto.form.RegisterCategoryForm;
import com.devqoo.backend.category.dto.response.CategoryResponseDto;
import com.devqoo.backend.common.response.CommonResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController implements CategoryApiDocs {

    @PostMapping
    public ResponseEntity<CommonResponse<CategoryResponseDto>> createCategory(
        @RequestBody @Valid RegisterCategoryForm registerCategoryForm
    ) {
        // TODO 관리자만 카테고리 생성 가능 (권한 검사하기)
        categoryService.create(registerCategoryForm);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<CategoryResponseDto>>> getCategories() {

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{categoryId}")
    public ResponseEntity<CommonResponse<CategoryResponseDto>> updateCategory(
        @PathVariable Long categoryId,
        @RequestBody @Valid RegisterCategoryForm registerCategoryForm
    ) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<CommonResponse<Void>> deleteCategory(@PathVariable Long categoryId) {
        return ResponseEntity.noContent().build();
    }
}
