package com.xbbg.springlab.catalog.category.repository;

import com.xbbg.springlab.catalog.category.model.Category;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryCategoryRepository implements CategoryRepository {

    private final ConcurrentMap<Long, Category> categories = new ConcurrentHashMap<>();
    private long nextId = 1L;

    @Override
    public synchronized Category save(Category category) {
        Category stored = copy(category);
        if (stored.getId() == null) {
            stored.setId(nextId++);
        }
        categories.put(stored.getId(), copy(stored));
        return copy(stored);
    }

    @Override
    public synchronized Optional<Category> findById(Long id) {
        return Optional.ofNullable(categories.get(id)).map(this::copy);
    }

    @Override
    public synchronized Optional<Category> findByCode(String code) {
        return categories.values().stream()
                .filter(category -> category.getCode().equals(code))
                .findFirst()
                .map(this::copy);
    }

    @Override
    public synchronized Optional<Category> findBySlug(String slug) {
        return categories.values().stream()
                .filter(category -> category.getSlug().equals(slug))
                .findFirst()
                .map(this::copy);
    }

    @Override
    public synchronized List<Category> findAll() {
        return categories.values().stream().map(this::copy).toList();
    }

    @Override
    public synchronized List<Category> findByParentCategoryId(Long parentCategoryId) {
        return categories.values().stream()
                .filter(category -> Objects.equals(parentCategoryId, category.getParentCategoryId()))
                .map(this::copy)
                .toList();
    }

    @Override
    public synchronized void deleteById(Long id) {
        categories.remove(id);
    }

    private Category copy(Category source) {
        Category target = new Category();
        target.setId(source.getId());
        target.setCode(source.getCode());
        target.setName(source.getName());
        target.setSlug(source.getSlug());
        target.setDescription(source.getDescription());
        target.setParentCategoryId(source.getParentCategoryId());
        target.setDisplayOrder(source.getDisplayOrder());
        target.setActive(source.isActive());
        target.setCreatedAt(source.getCreatedAt());
        target.setUpdatedAt(source.getUpdatedAt());
        return target;
    }
}
