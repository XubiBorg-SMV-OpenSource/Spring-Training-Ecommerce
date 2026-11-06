package com.xbbg.springlab.catalog.category.service;

import com.xbbg.springlab.catalog.category.dto.CategoryRequest;
import com.xbbg.springlab.catalog.category.dto.CategoryResponse;
import com.xbbg.springlab.catalog.category.dto.CategoryTreeResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse create(CategoryRequest request);

    List<CategoryResponse> list();

    CategoryResponse getById(Long id);

    CategoryResponse update(Long id, CategoryRequest request);

    CategoryResponse updateStatus(Long id, boolean active);

    void delete(Long id);

    List<CategoryTreeResponse> tree();
}
