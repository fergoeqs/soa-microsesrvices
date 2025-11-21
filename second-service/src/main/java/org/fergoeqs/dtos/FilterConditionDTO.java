package org.fergoeqs.dtos;

import javax.validation.constraints.NotNull;

public record FilterConditionDTO(
        @NotNull String field,
        @NotNull String operator,
        Object value
) {}