package com.library.library.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDate;

public record DtoLoan(
        Long id,
        Long bookId,
        String bookTitle,
        Long userId,
        String username,
        LocalDate loanDate,
        LocalDate dueDate,
        LocalDate returnDate
) {
}