package com.xbbg.springlab.catalog.category.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xbbg.springlab.catalog.category.model.Category;

import java.time.LocalDateTime;

public record CategoryResponse(
        Long id,
        String code,
        String name,
        String slug,
        String description,
        Long parentCategoryId,
        Integer displayOrder,
        boolean active,
        @JsonFormat(shape = JsonFormat.Shape.STRING) LocalDateTime createdAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING) LocalDateTime updatedAt
) {

    public static CategoryResponse from(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getCode(),
                category.getName(),
                category.getSlug(),
                category.getDescription(),
                category.getParentCategoryId(),
                category.getDisplayOrder(),
                category.isActive(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }
}
