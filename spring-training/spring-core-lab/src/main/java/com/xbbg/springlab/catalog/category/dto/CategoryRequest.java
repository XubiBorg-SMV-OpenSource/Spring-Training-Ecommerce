package com.xbbg.springlab.catalog.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record CategoryRequest(
        @NotBlank(message = "Code is required")
        @Pattern(
                regexp = "^[A-Za-z0-9][A-Za-z0-9_-]{1,49}$",
                message = "Code must be 2 to 50 letters, digits, underscores, or hyphens"
        )
        String code,

        @NotBlank(message = "Name is required")
        @Size(max = 120, message = "Name must be 120 characters or fewer")
        String name,

        @Size(max = 160, message = "Slug must be 160 characters or fewer")
        String slug,

        @Size(max = 2000, message = "Description must be 2000 characters or fewer")
        String description,

        Long parentCategoryId,

        @PositiveOrZero(message = "Display order cannot be negative")
        Integer displayOrder,

        @NotNull(message = "Active is required")
        Boolean active
) {
}
