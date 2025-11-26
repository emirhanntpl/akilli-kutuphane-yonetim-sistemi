package com.library.library.dto;

import java.time.LocalDateTime;

public record ErrorResponse(
        String message,
        String code, // Hata kodu alanı eklendi
        int statusCode,
        LocalDateTime timestamp
) {
}
