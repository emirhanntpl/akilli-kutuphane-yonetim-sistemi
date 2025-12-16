package com.library.library.controller;

import com.library.library.dto.CreateLoanRequest;
import com.library.library.dto.DtoLoan;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface LoansController {
    RootEntity<DtoLoan> borrowBook(@RequestBody CreateLoanRequest request);
    RootEntity<DtoLoan> getBorrowingDetails(@PathVariable("borrowingId") Long borrowingId);
    RootEntity<List<DtoLoan>> getLoansByUserId(@PathVariable("userId") Long userId);
    RootEntity<DtoLoan> returnBook(@PathVariable("borrowingId") Long borrowingId);
}
