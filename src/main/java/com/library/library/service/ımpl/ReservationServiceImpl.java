package com.library.library.service.ımpl;

import com.library.library.dto.DtoReservation;
import com.library.library.exception.BaseException;
import com.library.library.model.*;
import com.library.library.repository.BookRepository;
import com.library.library.repository.ReservationRepository;
import com.library.library.repository.UserRepository;
import com.library.library.service.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List; // EKLENDİ
import java.util.stream.Collectors; // EKLENDİ

import static com.library.library.exception.MessageType.*;

@Service
@AllArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Override
    public DtoReservation createReservation(Long userId, Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BaseException(INVALID_BOOK_NAME, HttpStatus.NOT_FOUND));

        if (book.getStock() > 0) {
            throw new BaseException(BOOK_IS_IN_STOCK_CANNOT_RESERVE, HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(USERNAME_NOT_FOUND, HttpStatus.NOT_FOUND));

        boolean alreadyReserved = reservationRepository.existsByUserIdAndBookIdAndStatus(userId, bookId, ReservationStatus.WAITING);
        if (alreadyReserved) {
            throw new BaseException(USER_ALREADY_HAS_RESERVATION_FOR_BOOK, HttpStatus.BAD_REQUEST);
        }

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setBook(book);
        reservation.setReservationDate(LocalDateTime.now());
        reservation.setStatus(ReservationStatus.WAITING);

        Reservation savedReservation = reservationRepository.save(reservation);

        return convertToDto(savedReservation);
    }


    @Override
    public List<DtoReservation> getUserReservations(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new BaseException(USERNAME_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        return reservationRepository.findByUserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    private DtoReservation convertToDto(Reservation reservation) {
        return new DtoReservation(
                reservation.getId(),
                reservation.getUser().getId(),
                reservation.getUser().getUsername(),
                reservation.getBook().getId(),
                reservation.getBook().getTitle(),
                reservation.getReservationDate(),
                reservation.getStatus()
        );
    }
}
