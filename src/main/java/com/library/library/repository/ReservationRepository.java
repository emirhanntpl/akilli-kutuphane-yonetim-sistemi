package com.library.library.repository;

import com.library.library.model.Reservation;
import com.library.library.model.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Optional<Reservation> findFirstByBookIdAndStatusOrderByReservationDateAsc(Long bookId, ReservationStatus status);
    
    boolean existsByUserIdAndBookIdAndStatus(Long userId, Long bookId, ReservationStatus status);

    Optional<Reservation> findByUserIdAndBookIdAndStatus(Long userId, Long bookId, ReservationStatus status);

    List<Reservation> findByUserId(Long userId);

    boolean existsByBookIdAndStatus(Long bookId, ReservationStatus status);


    List<Reservation> findByBookId(Long bookId);
}
