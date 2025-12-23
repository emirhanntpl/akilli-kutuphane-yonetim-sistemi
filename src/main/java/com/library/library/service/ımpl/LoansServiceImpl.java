package com.library.library.service.ımpl;

import com.library.library.dto.DtoLoan;
import com.library.library.exception.BaseException;
import com.library.library.model.*;
import com.library.library.repository.BookRepository;
import com.library.library.repository.LoansRepository;
import com.library.library.repository.ReservationRepository;
import com.library.library.repository.UserRepository;
import com.library.library.service.EmailService;
import com.library.library.service.LoansService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.library.library.exception.MessageType.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@AllArgsConstructor
public class LoansServiceImpl implements LoansService {

    private final LoansRepository loansRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final ReservationRepository reservationRepository;

    @Override
    public List<DtoLoan> getAllLoans() {
        return loansRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private DtoLoan convertToDto(Loan loan) {
        return new DtoLoan(
                loan.getId(),
                loan.getBook().getId(),
                loan.getBook().getTitle(),
                loan.getUser().getId(),
                loan.getUser().getUsername(),
                loan.getLoanDate(),
                loan.getDueDate(),
                loan.getReturnDate()
        );
    }

    @Override
    @Transactional
    public DtoLoan borrowBook(Long userId, Long bookId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(USERNAME_NOT_FOUND, BAD_REQUEST));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BaseException(INVALID_BOOK_NAME, BAD_REQUEST));


        Optional<Reservation> userReservation = reservationRepository.findByUserIdAndBookIdAndStatus(userId, bookId, ReservationStatus.NOTIFIED);

        if (book.getStock() <= 0) {

            if (userReservation.isEmpty()) {
                throw new BaseException(BOOK_NOT_IN_STOCK, HttpStatus.BAD_REQUEST);
            }
        }


        if (userReservation.isEmpty()) {
            book.setStock(book.getStock() - 1);
            bookRepository.save(book);
        } else {

            Reservation reservation = userReservation.get();
            reservation.setStatus(ReservationStatus.COMPLETED);
            reservationRepository.save(reservation);
        }

        Loan loan = new Loan();
        loan.setUser(user);
        loan.setBook(book);
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(14));
        loan.setReturnDate(null);
        Loan savedLoan = loansRepository.save(loan);

        String subject = "Kitap Ödünç Alma Bilgilendirmesi";
        String text = "Merhaba " + user.getName() + ",\n\n"
                    + "'" + book.getTitle() + "' adlı kitabı başarıyla ödünç aldınız.\n"
                    + "Son iade tarihi: " + savedLoan.getDueDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                    + "\n\nİyi okumalar!";
        emailService.sendEmail(user.getEmail(), subject, text);

        return convertToDto(savedLoan);
    }

    @Override
    public DtoLoan getBorrowingDetails(Long borrowingId) {
        Loan loan = loansRepository.findById(borrowingId).orElseThrow(() -> new BaseException(NO_SUCH_RECORD_FOUND, BAD_REQUEST));
        return convertToDto(loan);
    }

    @Override
    public List<DtoLoan> getLoansByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new BaseException(USERNAME_NOT_FOUND, BAD_REQUEST);
        }
        return loansRepository.findByUserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DtoLoan returnBook(Long borrowingId) {
        Loan loanReturn = loansRepository.findById(borrowingId).orElseThrow(() -> new BaseException(NO_SUCH_RECORD_FOUND, BAD_REQUEST));
        if (loanReturn.getReturnDate() != null) {
            throw new BaseException(BOOK_ALREADY_RETURNED, BAD_REQUEST);
        }

        Book book = loanReturn.getBook();
        User user = loanReturn.getUser();
        
        checkAndNotifyNextUserInQueue(book);

        LocalDate dueDate = loanReturn.getDueDate();
        LocalDate returnDate = LocalDate.now();
        String penaltyInfo = "";

        if (returnDate.isAfter(dueDate)) {
            long daysLate = ChronoUnit.DAYS.between(dueDate, returnDate);
            double penalty = daysLate * 1.0;
            user.addPenalty(penalty);
            userRepository.save(user);
            penaltyInfo = "\nKitabı " + daysLate + " gün gecikmeli teslim ettiğiniz için " + penalty + " TL ceza uygulanmıştır.";
        }

        loanReturn.setReturnDate(returnDate);
        Loan savedLoan = loansRepository.save(loanReturn);

        String subject = "Kitap İade Bilgilendirmesi";
        String text = "Merhaba " + user.getName() + ",\n\n"
                    + "'" + book.getTitle() + "' adlı kitabı başarıyla iade ettiniz."
                    + penaltyInfo
                    + "\n\nİyi günler dileriz.";
        emailService.sendEmail(user.getEmail(), subject, text);

        return convertToDto(savedLoan);
    }
    
    private void checkAndNotifyNextUserInQueue(Book book) {
        Optional<Reservation> nextInQueue = reservationRepository.findFirstByBookIdAndStatusOrderByReservationDateAsc(book.getId(), ReservationStatus.WAITING);

        if (nextInQueue.isPresent()) {
            Reservation reservation = nextInQueue.get();
            reservation.setStatus(ReservationStatus.NOTIFIED);
            reservation.setNotificationDate(LocalDateTime.now());
            reservationRepository.save(reservation);

            User userToNotify = reservation.getUser();
            String subject = "Beklediğiniz Kitap Geldi!";
            String text = "Merhaba " + userToNotify.getName() + ",\n\n"
                        + "Sırasında beklediğiniz '" + book.getTitle() + "' adlı kitap kütüphanemize geri döndü.\n"
                        + "Kitabı 24 saat içinde ödünç alabilirsiniz. Aksi takdirde rezervasyonunuz iptal edilecektir."
                        + "\n\nİyi günler dileriz.";
            emailService.sendEmail(userToNotify.getEmail(), subject, text);
        } else {
            book.setStock(book.getStock() + 1);
            bookRepository.save(book);
        }
    }
}
