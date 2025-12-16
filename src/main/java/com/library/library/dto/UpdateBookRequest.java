package com.library.library.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.Set;

public record UpdateBookRequest(
        @NotBlank
        String title,

        @NotBlank
        @Pattern(regexp = "^(?!0$).*", message = "ISBN 0 olamaz")
        String isbn,

        @NotNull
        @Min(value = 1, message = "Yayın yılı 0'dan büyük olmalıdır")
        Integer productionYear,

        @NotNull
        @Min(value = 1, message = "Sayfa sayısı 0'dan büyük olmalıdır")
        Integer pageOfNumber,

        @NotEmpty
        Set<@Min(value = 1, message = "Yazar ID 0'dan büyük olmalıdır") Long> authorIds,

        @NotEmpty
        Set<@Min(value = 1, message = "Kategori ID 0'dan büyük olmalıdır") Long> categoryIds,

        @NotNull
        @Min(value = 1, message = "Stok 0'dan büyük olmalıdır")
        Integer stock
) {
}
