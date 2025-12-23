package com.library.library.controller;

import com.library.library.dto.DtoBook;
import com.library.library.dto.DtoBookIU;
import com.library.library.dto.UpdateBookRequest;
import com.library.library.model.Book;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface BookController  {

    public RootEntity<Book> createBook(DtoBookIU dtoBookIU);


    RootEntity<String> deleteBook(Long bookId);

    public RootEntity<DtoBook> updateBook(Long bookId, UpdateBookRequest request);
}