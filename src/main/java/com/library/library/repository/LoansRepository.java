package com.library.library.repository;

import com.library.library.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoansRepository extends JpaRepository<Loan, Long> {
    boolean existsByBookIdAndReturnDateIsNull(Long bookId);
    List<Loan> findByUserId(Long userId);
    long countByReturnDateIsNull();

    // En çok ödünç alınan 5 kitap (Kitap ID ve Sayısı)
    @Query("SELECT l.book.title, COUNT(l) as count FROM Loan l GROUP BY l.book.title ORDER BY count DESC LIMIT 5")
    List<Object[]> findMostPopularBooks();

    // En çok kitap ödünç alan 5 kullanıcı (Kullanıcı Adı ve Sayısı)
    @Query("SELECT l.user.username, COUNT(l) as count FROM Loan l GROUP BY l.user.username ORDER BY count DESC LIMIT 5")
    List<Object[]> findMostActiveUsers();
}
