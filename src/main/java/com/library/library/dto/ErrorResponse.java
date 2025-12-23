package com.library.library.dto;

import java.time.LocalDateTime;

public record ErrorResponse(
        String message,
        String code,
        int statusCode,
        LocalDateTime timestamp
) {
}
