package com.xbbg.springlab.catalog.category.dto;

import java.util.List;

public record CategoryTreeResponse(
        Long id,
        String code,
        String name,
        String slug,
        Long parentCategoryId,
        Integer displayOrder,
        boolean active,
        List<CategoryTreeResponse> children
) {
}
