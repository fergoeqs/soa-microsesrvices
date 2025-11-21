package org.fergoeqs.dtos;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public record SortOptionDTO(
        @NotNull String field,
        String direction,
        @Min(1) Integer priority
) {}