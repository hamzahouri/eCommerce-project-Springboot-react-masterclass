package com.ecommerce.project.category.service;

import com.ecommerce.project.category.model.Category;
import com.ecommerce.project.category.payload.CategoryDto;
import com.ecommerce.project.category.payload.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse getAllCategories(Integer pageNumber, Integer pagSize);
    CategoryDto creatCategory(CategoryDto categoryDto);

    String deleteCategory(Long categoryId);

    CategoryDto updateCategory(CategoryDto category, Long categoryId);
}
