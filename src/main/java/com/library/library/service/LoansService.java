package com.library.library.service;

import com.library.library.dto.DtoLoan;
import com.library.library.model.User;

import java.util.List;

public interface LoansService {

    public DtoLoan borrowBook(Long userId, Long bookId);
    DtoLoan getBorrowingDetails(Long borrowingId);
    List<DtoLoan> getLoansByUserId(Long userId);
    DtoLoan returnBook(Long borrowingId);

}



