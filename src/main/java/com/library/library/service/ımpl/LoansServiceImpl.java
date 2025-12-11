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
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@Service
public class LoansServiceImpl implements LoansService {


     private  final LoansRepository loansRepository;
     private final BookRepository bookRepository;
     private final UserRepository userRepository;


    public LoansServiceImpl(LoansRepository loansRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.loansRepository = loansRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Override
    public DtoLoan borrowBook(Long userId, Long bookId) {
        User user=userRepository.findById(userId).orElseThrow(()-> new BaseException(MessageType.USERNAME_NOT_FOUND,BAD_REQUEST));
        Book book=bookRepository.findById(bookId).orElseThrow(()-> new BaseException(MessageType.INVALID_BOOK_NAME,BAD_REQUEST));
        boolean isBookOnLoan = loansRepository.existsByBookIdAndReturnDateIsNull(bookId);
        if (isBookOnLoan) {
            throw new BaseException(MessageType.BOOK_ALREADY_ON_LOAN, HttpStatus.BAD_REQUEST);
        }
        Loan loan=new Loan();
        loan.setUser(user);
        loan.setBook(book);
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(14));
        loan.setReturnDate(null);
        Loan savedLoan = loansRepository.save(loan);
        return new DtoLoan(
                savedLoan.getId(),
                savedLoan.getBook().getId(),
                savedLoan.getBook().getTitle(),
                savedLoan.getUser().getId(),
                savedLoan.getUser().getUsername(),
                savedLoan.getLoanDate(),
                savedLoan.getDueDate(),
                savedLoan.getReturnDate()
        );
    }

    @Override
    public DtoLoan getBorrowingDetails(Long borrowingId) {
        Loan loans = loansRepository.findById(borrowingId).orElseThrow(() -> new BaseException(MessageType.NO_SUCH_RECORD_FOUND, BAD_REQUEST));


        return new DtoLoan(
                loans.getId(),
                loans.getBook().getId(),
                loans.getBook().getTitle(),
                loans.getUser().getId(),
                loans.getUser().getUsername(),
                loans.getLoanDate(),
                loans.getDueDate(),
                loans.getReturnDate()
        );
    }

    @Override
    public List<DtoLoan> getLoansByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new BaseException(MessageType.USERNAME_NOT_FOUND, BAD_REQUEST);
        }
        List<Loan> userLoans = loansRepository.findByUserId(userId);
        List<DtoLoan> dtoLoans = new ArrayList<>();
        for (Loan loan : userLoans) {
            DtoLoan dtoLoan = new DtoLoan(
                    loan.getId(),
                    loan.getBook().getId(),
                    loan.getBook().getTitle(),
                    loan.getUser().getId(),
                    loan.getUser().getUsername(),
                    loan.getLoanDate(),
                    loan.getDueDate(),
                    loan.getReturnDate()
            );
            dtoLoans.add(dtoLoan);

        }
        return dtoLoans;
    }

    @Override
    public DtoLoan returnBook(Long borrowingId) {
        Loan loanReturn = loansRepository.findById(borrowingId).orElseThrow(() -> new BaseException(MessageType.NO_SUCH_RECORD_FOUND, BAD_REQUEST));
        if (loanReturn.getReturnDate() != null) {
            throw new BaseException(MessageType.BOOK_ALREADY_ON_LOAN, BAD_REQUEST);
        }
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


        return new DtoLoan(
                savedLoan.getId(),
                savedLoan.getBook().getId(),
                savedLoan.getBook().getTitle(),
                savedLoan.getUser().getId(),
                savedLoan.getUser().getUsername(),
                savedLoan.getLoanDate(),
                savedLoan.getDueDate(),
                savedLoan.getReturnDate()
        );
    }
}
