package com.library.library.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record UpdateBookRequest(
        @NotBlank
        String title,

        @NotBlank
        String isbn,

        @NotNull
        Integer productionYear,

        @NotNull
        Integer pageOfNumber,

        @NotEmpty
        Set<Long> authorIds,

        @NotEmpty
        Set<Long> categoryIds
) {
}
