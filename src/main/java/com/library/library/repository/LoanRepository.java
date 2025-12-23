package com.library.library.repository;

import com.library.library.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    boolean existsByBookIdAndReturnDateIsNull(Long bookId);
    List<Loan> findByUserId(Long userId);
    long countByReturnDateIsNull();

    List<Loan> findByDueDateAndReturnDateIsNull(LocalDate dueDate);

    @Query("SELECT l.book.title, COUNT(l) as count FROM Loan l GROUP BY l.book.title ORDER BY count DESC LIMIT 5")
    List<Object[]> findMostPopularBooks();

    @Query("SELECT l.user.username, COUNT(l) as count FROM Loan l GROUP BY l.user.username ORDER BY count DESC LIMIT 5")
    List<Object[]> findMostActiveUsers();
    
    List<Loan> findByUserIdAndReturnDateIsNull(Long userId);


    List<Loan> findByBookId(Long bookId);
}
