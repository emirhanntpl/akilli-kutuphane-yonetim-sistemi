package com.library.library.controller.Impl;

import com.library.library.controller.BookController;
import com.library.library.controller.RestBaseController;
import com.library.library.controller.RootEntity;
import com.library.library.dto.DtoBook;
import com.library.library.dto.DtoBookIU;
import com.library.library.dto.UpdateBookRequest;
import com.library.library.model.Book;
import com.library.library.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("rest/api/books")
public class BookControllerImpl extends RestBaseController implements BookController {

    private final BookService bookService;

    public BookControllerImpl(BookService bookService) {
        this.bookService = bookService;

    }

    @PostMapping("/create")
    @Override
    public RootEntity<Book> createBook(@RequestBody @Valid DtoBookIU dtoBookIU) {
        return ok(bookService.createBook(dtoBookIU));
    }

    @PostMapping("/delete/{id}")
    @Override
    public ResponseEntity<Void> deleteBook(@RequestBody @Valid DtoBookIU dtoBookIU) {
        bookService.deleteBook(dtoBookIU);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/update/{id}")
    @Override
    public RootEntity<DtoBook> updateBook(@RequestBody @Valid Long bookId, UpdateBookRequest request) {
        return ok(bookService.updateBook(bookId, request));
    }
}
