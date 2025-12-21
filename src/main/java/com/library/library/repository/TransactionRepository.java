package com.library.library.repository;

import com.library.library.model.Transaction;
import com.library.library.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByOrderByTransactionDateDesc();

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.type = :type")
    Double sumAmountByType(TransactionType type);
}
