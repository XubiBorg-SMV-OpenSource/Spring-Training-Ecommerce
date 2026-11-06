package com.xbbg.springlab.catalog.category.dto;

import jakarta.validation.constraints.NotNull;

public record CategoryStatusRequest(
        @NotNull(message = "Active is required")
        Boolean active
) {
}
