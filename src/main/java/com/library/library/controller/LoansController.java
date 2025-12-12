package com.library.library.controller;

import com.library.library.dto.DtoLoan;
import com.library.library.dto.CreateLoanRequest;

import java.util.List;

public interface LoansController {

    public RootEntity<DtoLoan> borrowBook(CreateLoanRequest request);

    public RootEntity<DtoLoan> getBorrowingDetails(Long borrowingId);

    public RootEntity<List<DtoLoan>> getLoansByUserId(Long userId);

    public RootEntity<DtoLoan> returnBook(Long borrowingId);




}
