package com.library.library.service;

import com.library.library.dto.DtoBook;
import com.library.library.dto.DtoBookIU;
import com.library.library.dto.UpdateBookRequest;
import com.library.library.model.Book;

public interface BookService {

    public Book createBook(DtoBookIU dtoBookIU);

    public  void  deleteBook(DtoBookIU dtoBookIU);


    public DtoBook updateBook(Long bookId, UpdateBookRequest request);
}
