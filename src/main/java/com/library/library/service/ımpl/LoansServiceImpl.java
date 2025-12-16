package com.library.library.service.ımpl;

import com.library.library.dto.DtoLoan;
import com.library.library.exception.BaseException;
import com.library.library.exception.MessageType;
import com.library.library.model.Book;
import com.library.library.model.Loan;
import com.library.library.model.User;
import com.library.library.repository.BookRepository;
import com.library.library.repository.LoansRepository;
import com.library.library.repository.UserRepository;
import com.library.library.service.LoansService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.library.library.exception.MessageType.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
public class LoansServiceImpl implements LoansService {

    private final LoansRepository loansRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public LoansServiceImpl(LoansRepository loansRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.loansRepository = loansRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

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


        if (book.getStock() <= 0) {
            throw new BaseException(BOOK_NOT_IN_STOCK, HttpStatus.BAD_REQUEST);
        }
        

        book.setStock(book.getStock() - 1);
        bookRepository.saveAndFlush(book);

        Loan loan = new Loan();
        loan.setUser(user);
        loan.setBook(book);
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(14));
        loan.setReturnDate(null);
        Loan savedLoan = loansRepository.save(loan);

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

        Long bookId = loanReturn.getBook().getId();
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BaseException(INVALID_BOOK_NAME, BAD_REQUEST));
        
        book.setStock(book.getStock() + 1);
        bookRepository.saveAndFlush(book);

        LocalDate dueDate = loanReturn.getDueDate();
        LocalDate returnDate = LocalDate.now();

        if (returnDate.isAfter(dueDate)) {
            long daysLate = ChronoUnit.DAYS.between(dueDate, returnDate);
            double penalty = daysLate * 1.0;

            User user = loanReturn.getUser();
            user.addPenalty(penalty);
            userRepository.save(user);
        }

        loanReturn.setReturnDate(returnDate);
        Loan savedLoan = loansRepository.save(loanReturn);

        return convertToDto(savedLoan);
    }
}
