package org.example.backend.services;

import jakarta.validation.Valid;
import org.example.backend.payload.CategoryDTO;
import org.example.backend.payload.CategoryResponse;

public interface CategoryService {
    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String orderBy, String orderDir);

    CategoryDTO createCategory(@Valid CategoryDTO categoryDTO);

    CategoryDTO updateCategory(Long categoryId, @Valid CategoryDTO categoryDTO);

    CategoryDTO deleteCategory(Long categoryId);
}
