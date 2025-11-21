package org.fergoeqs.dtos;

import javax.validation.constraints.Min;
import java.util.List;

public record FilterRequestDTO(
        List<FilterConditionDTO> filters,
        List<SortOptionDTO> sort,
        @Min(0) Integer page,
        @Min(1) Integer size
) {}