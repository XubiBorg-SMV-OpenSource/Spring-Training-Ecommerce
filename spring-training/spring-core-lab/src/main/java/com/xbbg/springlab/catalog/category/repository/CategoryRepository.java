package com.xbbg.springlab.catalog.category.repository;

import com.xbbg.springlab.catalog.category.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    Category save(Category category);

    Optional<Category> findById(Long id);

    Optional<Category> findByCode(String code);

    Optional<Category> findBySlug(String slug);

    List<Category> findAll();

    List<Category> findByParentCategoryId(Long parentCategoryId);

    void deleteById(Long id);
}
