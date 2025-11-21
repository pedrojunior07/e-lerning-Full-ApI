package com.elearning.e_learning_core.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elearning.e_learning_core.Dtos.ApiResponse;
import com.elearning.e_learning_core.Dtos.CategoryDTO;
import com.elearning.e_learning_core.service.CategoryService;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> create(@RequestBody CategoryDTO dto) {

        ApiResponse<?> response = categoryService.createCategory(dto);
        return ResponseEntity.status(response.getCode())
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> update(@PathVariable Long id, @RequestBody CategoryDTO dto) {
        return ResponseEntity.status(categoryService.updateCategory(id, dto).getCode())
                .body(categoryService.updateCategory(id, dto));
    }

    @GetMapping("/{id}")
    public CategoryDTO getById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAll() {
        ApiResponse<?> response = categoryService.getAllCategories();
        return ResponseEntity.status(response.getCode()).body(response);

    }


    @GetMapping("/categories-with-numbers")
    public ResponseEntity<ApiResponse<?>> getCategoriesWithNumberOfCources() {
        ApiResponse<?> response = categoryService.getCategoriesWithNumberOfCources();
        return ResponseEntity.status(response.getCode()).body(response);

    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}