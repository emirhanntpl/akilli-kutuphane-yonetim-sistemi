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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("rest/api/books")
public class BookControllerImpl extends RestBaseController implements BookController {

    private final BookService bookService;

    public BookControllerImpl(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/update-all-stocks")
    public RootEntity<String> updateAllStocks() {
        bookService.updateAllStocks(10);
        return ok("Tüm kitapların stoğu 10 olarak güncellendi.");
    }

    @GetMapping
    public RootEntity<List<DtoBook>> getAllBooks() {
        return ok(bookService.getAllBooks());
    }


    @GetMapping("/category/{categoryId}")
    public RootEntity<List<DtoBook>> getBooksByCategoryId(@PathVariable Long categoryId) {
        return ok(bookService.getBooksByCategoryId(categoryId));
    }

    @PostMapping("/create")
    @Override
    public RootEntity<Book> createBook(@RequestBody @Valid DtoBookIU dtoBookIU) {
        return ok(bookService.createBook(dtoBookIU));
    }

    @DeleteMapping("/delete/{id}")
    @Override
    public RootEntity<String> deleteBook(@PathVariable("id") Long bookId ) {
        DtoBookIU dto = new DtoBookIU();
        dto.setId(bookId);
        bookService.deleteBook(dto);
        return ok("Kitap başarıyla silindi.");
    }

    @PutMapping("/update/{id}")
    @Override
    public RootEntity<DtoBook> updateBook(@PathVariable("id") Long bookId, @RequestBody @Valid UpdateBookRequest request) {
        return ok(bookService.updateBook(bookId, request));
    }
}
