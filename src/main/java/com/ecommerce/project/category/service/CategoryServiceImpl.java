package com.ecommerce.project.category.service;

import com.ecommerce.project.category.model.Category;
import com.ecommerce.project.category.payload.CategoryDto;
import com.ecommerce.project.category.payload.CategoryResponse;
import com.ecommerce.project.category.repository.CategoryRepository;
import com.ecommerce.project.exceptions.ApiException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pagSize) {

        Pageable pageable = PageRequest.of(pageNumber,pagSize);
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        List<Category> categories = categoryPage.getContent();
        if (categories.isEmpty()) throw new ApiException("No categories added");

        List<CategoryDto> categoryDtos = categories.stream().map(cat -> modelMapper.map(cat,CategoryDto.class))
                .collect(Collectors.toList());

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDtos);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());




        return categoryResponse;
    }

    @Override
    public CategoryDto creatCategory(CategoryDto categoryDto) {

        Category category = modelMapper.map(categoryDto, Category.class);
        Category savedCategory = categoryRepository.findCategoryByCategoryName(category.getCategoryName());
        if(savedCategory !=null) throw new ApiException("Categorie with the name "+ category.getCategoryName() +" already exist");

        Category cat = categoryRepository.save(category);
        CategoryDto dto = modelMapper.map(cat,CategoryDto.class);
        return dto;
    }

    @Override
    public String deleteCategory(Long categoryId) {

        Category categorie = categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category", "categoryId",categoryId ));
        categoryRepository.delete(categorie);

        return "category deleted";
    }

    @Override
    public CategoryDto updateCategory(CategoryDto category, Long categoryId) {

        Category savedCategory = categoryRepository.findById(categoryId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Category cat = modelMapper.map(category,Category.class);
        savedCategory.setCategoryName(category.getCategoryName());

        Category savedCat = categoryRepository.save(cat);
        return modelMapper.map(savedCat, CategoryDto.class);
    }
}
