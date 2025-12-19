package com.library.library.controller;

import com.library.library.dto.DtoReservation;
import com.library.library.service.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; // EKLENDİ

import java.util.List; // EKLENDİ

@RestController
@RequestMapping("/rest/api/reservations")
@AllArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/user/{userId}/book/{bookId}")
    public ResponseEntity<DtoReservation> createReservation(@PathVariable Long userId, @PathVariable Long bookId) {
        return ResponseEntity.ok(reservationService.createReservation(userId, bookId));
    }

    // YENİ EKLENDİ
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<DtoReservation>> getUserReservations(@PathVariable Long userId) {
        return ResponseEntity.ok(reservationService.getUserReservations(userId));
    }
}
