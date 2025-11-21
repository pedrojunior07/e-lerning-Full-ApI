package com.elearning.e_learning_core.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elearning.e_learning_core.Dtos.ApiResponse;
import com.elearning.e_learning_core.Dtos.CategoryDTO;
import com.elearning.e_learning_core.Repository.CategoryRepository;
import com.elearning.e_learning_core.model.Category;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    private CategoryDTO convertToDTO(Category category) {
        return new CategoryDTO(category.getId(), category.getName(), null);
    }

    private Category convertToEntity(CategoryDTO dto) {
        Category category = new Category();
        category.setId(dto.getId());
        category.setName(dto.getName());
        return category;
    }

    public ApiResponse<?> createCategory(CategoryDTO dto) {
        Category saved = categoryRepository.save(convertToEntity(dto));
        return new ApiResponse<>("Category created successfully", "", 201,
                convertToDTO(saved));
    }

    public ApiResponse<?> updateCategory(Long id, CategoryDTO dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        category.setName(dto.getName());
        return new ApiResponse<>("Category updated successfully", "", 200,
                convertToDTO(categoryRepository.save(category)));
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    public CategoryDTO getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public ApiResponse<?> getAllCategories() {
        return new ApiResponse<>("Categories retrieved successfully", "", 200,
                categoryRepository.findAll().stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList()));
    }

    public ApiResponse<?> getCategoriesWithNumberOfCources() {
        return new ApiResponse<>("Categories retrieved successfully", "", 200,
                categoryRepository.countCoursesByCategory());
    }

}
