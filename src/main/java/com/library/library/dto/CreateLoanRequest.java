package com.library.library.dto;

import jakarta.validation.constraints.NotNull;

public class CreateLoanRequest {

    @NotNull
    private Long userId;

    @NotNull
    private Long bookId;

    public CreateLoanRequest() {
    }

    public CreateLoanRequest(Long userId, Long bookId) {
        this.userId = userId;
        this.bookId = bookId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
}

