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
public class LoansControllerImpl extends RestBaseController implements LoansController {

    private final    LoansService loansService;

    public LoansControllerImpl(LoansService loansService) {
        this.loansService = loansService;
    }

    @PostMapping("/rest/api/loans")
    @Override
    public RootEntity<DtoLoan> borrowBook(@RequestBody @Valid CreateLoanRequest request) {
        return ok(loansService.borrowBook(request.getUserId(), request.getBookId()));
    }

    @GetMapping("rest/api/loans/{borrowingId}")
    @Override
    public RootEntity<DtoLoan> getBorrowingDetails(@PathVariable("id") @RequestBody @Valid Long borrowingId) {
        return ok(loansService.returnBook(borrowingId));
    }

    @Override
    public RootEntity<List<DtoLoan>> getLoansByUserId(Long userId) {
        return null;
    }

    @Override
    public RootEntity<DtoLoan> returnBook(Long borrowingId) {
        return null;
    }
}
