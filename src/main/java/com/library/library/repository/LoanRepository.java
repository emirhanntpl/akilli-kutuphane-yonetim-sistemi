package com.library.library.repository;

import com.library.library.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByUserIdAndReturnDateIsNull(Long userId);


    boolean existsByBookIdAndReturnDateIsNull(Long bookId);
}
