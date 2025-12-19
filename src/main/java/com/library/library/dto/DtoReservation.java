package com.library.library.dto;

import com.library.library.model.ReservationStatus;

import java.time.LocalDateTime;

public record DtoReservation(
        Long id,
        Long userId,
        String username,
        Long bookId,
        String bookTitle,
        LocalDateTime reservationDate,
        ReservationStatus status
) {
}
