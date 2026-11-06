package com.xbbg.springlab.catalog.category.service.impl;

import com.xbbg.springlab.catalog.category.dto.CategoryRequest;
import com.xbbg.springlab.catalog.category.dto.CategoryResponse;
import com.xbbg.springlab.catalog.category.dto.CategoryTreeResponse;
import com.xbbg.springlab.catalog.category.model.Category;
import com.xbbg.springlab.catalog.category.repository.CategoryRepository;
import com.xbbg.springlab.catalog.category.service.CategoryService;
import com.xbbg.springlab.exception.BadRequestException;
import com.xbbg.springlab.exception.ConflictException;
import com.xbbg.springlab.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final Comparator<Category> DISPLAY_ORDER = Comparator
            .comparing((Category category) -> category.getDisplayOrder() == null ? 0 : category.getDisplayOrder())
            .thenComparing(Category::getName, String.CASE_INSENSITIVE_ORDER);

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryResponse create(CategoryRequest request) {
        String code = normalizeCode(request.code());
        String slug = slug(request.slug(), request.name());
        ensureCodeAvailable(code, null);
        ensureSlugAvailable(slug, null);
        ensureParentExists(request.parentCategoryId());

        LocalDateTime now = LocalDateTime.now();
        Category category = new Category();
        category.setCode(code);
        category.setName(request.name().trim());
        category.setSlug(slug);
        category.setDescription(blankToNull(request.description()));
        category.setParentCategoryId(request.parentCategoryId());
        category.setDisplayOrder(request.displayOrder() == null ? 0 : request.displayOrder());
        category.setActive(request.active());
        category.setCreatedAt(now);
        category.setUpdatedAt(now);
        return CategoryResponse.from(categoryRepository.save(category));
    }

    @Override
    public List<CategoryResponse> list() {
        return categoryRepository.findAll().stream()
                .sorted(DISPLAY_ORDER)
                .map(CategoryResponse::from)
                .toList();
    }

    @Override
    public CategoryResponse getById(Long id) {
        return CategoryResponse.from(requireCategory(id));
    }

    @Override
    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = requireCategory(id);
        String code = normalizeCode(request.code());
        String slug = slug(request.slug(), request.name());
        ensureCodeAvailable(code, id);
        ensureSlugAvailable(slug, id);
        ensureParentExists(request.parentCategoryId());
        ensureNoCycle(id, request.parentCategoryId());

        category.setCode(code);
        category.setName(request.name().trim());
        category.setSlug(slug);
        category.setDescription(blankToNull(request.description()));
        category.setParentCategoryId(request.parentCategoryId());
        category.setDisplayOrder(request.displayOrder() == null ? 0 : request.displayOrder());
        category.setActive(request.active());
        category.setUpdatedAt(LocalDateTime.now());
        return CategoryResponse.from(categoryRepository.save(category));
    }

    @Override
    public CategoryResponse updateStatus(Long id, boolean active) {
        Category category = requireCategory(id);
        category.setActive(active);
        category.setUpdatedAt(LocalDateTime.now());
        return CategoryResponse.from(categoryRepository.save(category));
    }

    @Override
    public void delete(Long id) {
        requireCategory(id);
        if (!categoryRepository.findByParentCategoryId(id).isEmpty()) {
            throw new ConflictException("Category " + id + " still has child categories");
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public List<CategoryTreeResponse> tree() {
        return categoryRepository.findByParentCategoryId(null).stream()
                .sorted(DISPLAY_ORDER)
                .map(this::toTree)
                .toList();
    }

    private CategoryTreeResponse toTree(Category category) {
        List<CategoryTreeResponse> children = categoryRepository.findByParentCategoryId(category.getId()).stream()
                .sorted(DISPLAY_ORDER)
                .map(this::toTree)
                .toList();
        return new CategoryTreeResponse(
                category.getId(),
                category.getCode(),
                category.getName(),
                category.getSlug(),
                category.getParentCategoryId(),
                category.getDisplayOrder(),
                category.isActive(),
                children
        );
    }

    private Category requireCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category " + id + " was not found"));
    }

    private void ensureParentExists(Long parentCategoryId) {
        if (parentCategoryId != null && categoryRepository.findById(parentCategoryId).isEmpty()) {
            throw new NotFoundException("Parent category " + parentCategoryId + " was not found");
        }
    }

    private void ensureNoCycle(Long categoryId, Long parentCategoryId) {
        Long current = parentCategoryId;
        while (current != null) {
            if (current.equals(categoryId)) {
                throw new BadRequestException("Category " + categoryId + " cannot be placed under itself or its own child");
            }
            current = categoryRepository.findById(current)
                    .map(Category::getParentCategoryId)
                    .orElse(null);
        }
    }

    private void ensureCodeAvailable(String code, Long currentId) {
        categoryRepository.findByCode(code).ifPresent(existing -> {
            if (!existing.getId().equals(currentId)) {
                throw new ConflictException("Category code " + code + " is already in use");
            }
        });
    }

    private void ensureSlugAvailable(String slug, Long currentId) {
        categoryRepository.findBySlug(slug).ifPresent(existing -> {
            if (!existing.getId().equals(currentId)) {
                throw new ConflictException("Category slug " + slug + " is already in use");
            }
        });
    }

    private String normalizeCode(String code) {
        return code.trim().toUpperCase(Locale.ROOT);
    }

    private String slug(String requested, String name) {
        String source = requested == null || requested.isBlank() ? name : requested;
        String slug = source.toLowerCase(Locale.ROOT)
                .trim()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");
        if (slug.isBlank()) {
            throw new BadRequestException("Slug cannot be empty");
        }
        return slug;
    }

    private String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
