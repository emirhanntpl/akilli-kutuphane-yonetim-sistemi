package com.library.library.controller;

import com.library.library.dto.DtoBook;
import com.library.library.dto.DtoBookIU;
import com.library.library.dto.UpdateBookRequest;
import com.library.library.model.Book;
import org.springframework.http.ResponseEntity;

public interface BookController  {

    public RootEntity<Book> createBook(DtoBookIU dtoBookIU);

    ResponseEntity<Void> deleteBook(DtoBookIU dtoBookIU);

    public RootEntity<DtoBook> updateBook(Long bookId, UpdateBookRequest request);
}
