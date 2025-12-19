package com.library.library.dto;

import com.library.library.model.TransactionType;
import java.time.LocalDateTime;

public record DtoTransaction(
        Long id,
        String username,
        Double amount,
        String description,
        LocalDateTime date,
        TransactionType type
) {}
