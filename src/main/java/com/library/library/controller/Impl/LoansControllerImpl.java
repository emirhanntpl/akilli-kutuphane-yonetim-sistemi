package com.library.library.controller.Impl;

import com.library.library.controller.LoansController;
import com.library.library.controller.RestBaseController;
import com.library.library.controller.RootEntity;
import com.library.library.dto.DtoLoan;
import com.library.library.dto.CreateLoanRequest;
import com.library.library.service.LoansService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/api/loans")
public class LoansControllerImpl extends RestBaseController implements LoansController {

    private final LoansService loansService;

    public LoansControllerImpl(LoansService loansService) {
        this.loansService = loansService;
    }


    @GetMapping
    public RootEntity<List<DtoLoan>> getAllLoans() {
        return ok(loansService.getAllLoans());
    }

    @PostMapping
    @Override
    public RootEntity<DtoLoan> borrowBook(@RequestBody @Valid CreateLoanRequest request) {
        return ok(loansService.borrowBook(request.getUserId(), request.getBookId()));
    }

    @GetMapping("/{borrowingId}")
    @Override
    public RootEntity<DtoLoan> getBorrowingDetails(@PathVariable("borrowingId") Long borrowingId) {
        return ok(loansService.getBorrowingDetails(borrowingId));
    }

    @GetMapping("/user/{userId}")
    @Override
    public RootEntity<List<DtoLoan>> getLoansByUserId(@PathVariable("userId") Long userId) {
        return ok(loansService.getLoansByUserId(userId));
    }

    @PutMapping("/returnBook/{borrowingId}")
    @Override
    public RootEntity<DtoLoan> returnBook(@PathVariable("borrowingId") Long borrowingId) {
        return ok(loansService.returnBook(borrowingId));
    }
}