package com.library.library.service;

import com.library.library.dto.DtoReservation;
import java.util.List; // EKLENDİ

public interface ReservationService {
    DtoReservation createReservation(Long userId, Long bookId);
    List<DtoReservation> getUserReservations(Long userId); // EKLENDİ
}
