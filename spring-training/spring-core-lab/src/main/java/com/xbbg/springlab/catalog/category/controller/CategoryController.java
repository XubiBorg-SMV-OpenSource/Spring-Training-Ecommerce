package com.xbbg.springlab.catalog.category.controller;

import com.xbbg.springlab.catalog.category.dto.CategoryRequest;
import com.xbbg.springlab.catalog.category.dto.CategoryResponse;
import com.xbbg.springlab.catalog.category.dto.CategoryStatusRequest;
import com.xbbg.springlab.catalog.category.dto.CategoryTreeResponse;
import com.xbbg.springlab.catalog.category.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/v1/catalog/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryRequest request) {
        CategoryResponse created = categoryService.create(request);
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping
    public List<CategoryResponse> list() {
        return categoryService.list();
    }

    @GetMapping("/tree")
    public List<CategoryTreeResponse> tree() {
        return categoryService.tree();
    }

    @GetMapping("/{id}")
    public CategoryResponse get(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    @PutMapping("/{id}")
    public CategoryResponse update(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
        return categoryService.update(id, request);
    }

    @PatchMapping("/{id}/status")
    public CategoryResponse updateStatus(@PathVariable Long id, @Valid @RequestBody CategoryStatusRequest request) {
        return categoryService.updateStatus(id, request.active());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/products")
    public List<Object> products(@PathVariable Long id) {
        categoryService.getById(id);
        return List.of();
    }
}
