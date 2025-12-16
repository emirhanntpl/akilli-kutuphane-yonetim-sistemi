package com.library.library.service;

import com.library.library.dto.DtoLoan;

import java.util.List;

public interface LoansService {
    DtoLoan borrowBook(Long userId, Long bookId);
    DtoLoan getBorrowingDetails(Long borrowingId);
    List<DtoLoan> getLoansByUserId(Long userId);
    DtoLoan returnBook(Long borrowingId);

    // YENİ EKLENEN METOT
    List<DtoLoan> getAllLoans();
}
