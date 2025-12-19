package com.library.library.repository;

import com.library.library.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate; // EKLENDİ
import java.util.List;

@Repository
public interface LoansRepository extends JpaRepository<Loan, Long> {
    boolean existsByBookIdAndReturnDateIsNull(Long bookId);
    List<Loan> findByUserId(Long userId);
    long countByReturnDateIsNull();

    // Belirtilen teslim tarihine sahip ve iade edilmemiş ödünçleri bulur
    List<Loan> findByDueDateAndReturnDateIsNull(LocalDate dueDate); // EKLENDİ

    // En çok ödünç alınan 5 kitap (Kitap ID ve Sayısı)
    @Query("SELECT l.book.title, COUNT(l) as count FROM Loan l GROUP BY l.book.title ORDER BY count DESC LIMIT 5")
    List<Object[]> findMostPopularBooks();

    // En çok kitap ödünç alan 5 kullanıcı (Kullanıcı Adı ve Sayısı)
    @Query("SELECT l.user.username, COUNT(l) as count FROM Loan l GROUP BY l.user.username ORDER BY count DESC LIMIT 5")
    List<Object[]> findMostActiveUsers();
    
    List<Loan> findByUserIdAndReturnDateIsNull(Long userId); // Bu satır UserService'deki bir hata için eklendi
}
