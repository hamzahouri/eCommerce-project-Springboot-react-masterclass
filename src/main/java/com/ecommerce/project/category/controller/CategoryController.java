package com.ecommerce.project.category.controller;

import com.ecommerce.project.category.model.Category;
import com.ecommerce.project.category.payload.CategoryDto;
import com.ecommerce.project.category.payload.CategoryResponse;
import com.ecommerce.project.category.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getCategories (
            @RequestParam(name = "pageNumber") Integer pageNumber,
            @RequestParam(name = "pageSize") Integer pageSize
    ) {
        CategoryResponse categoryResponse = categoryService.getAllCategories(pageNumber,pageSize);
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }

    @PostMapping("/public/categories")
    public ResponseEntity<CategoryDto> addCategory (@Valid  @RequestBody CategoryDto categoryDto) {

        CategoryDto dto = categoryService.creatCategory(categoryDto);
        return new ResponseEntity<>(dto,HttpStatus.OK);

    }
    @DeleteMapping("/admin/delete/{categoryId}")
    public String deleteCategory (@PathVariable Long categoryId) {
        String status = categoryService.deleteCategory(categoryId);
        return status;
    }

    @PutMapping("/update/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory (@RequestBody CategoryDto categoryDto,
                                                  @PathVariable Long categoryId) {


    CategoryDto upadetdCategory = categoryService.updateCategory(categoryDto,categoryId );
    return new ResponseEntity<>(upadetdCategory,HttpStatus.OK);


    }
}
