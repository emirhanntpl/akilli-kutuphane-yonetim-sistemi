package com.library.library.service;

import com.library.library.dto.DtoBook;
import com.library.library.dto.DtoBookIU;
import com.library.library.dto.UpdateBookRequest;
import com.library.library.model.Book;

import java.util.List;

public interface BookService {
    Book createBook(DtoBookIU dtoBookIU);
    void deleteBook(DtoBookIU dtoBookIU);
    DtoBook updateBook(Long bookId, UpdateBookRequest request);
    List<DtoBook> getAllBooks();
    void updateAllStocks(Integer stock);

    // YENİ EKLENEN METOT
    List<DtoBook> getBooksByCategoryId(Long categoryId);
}
