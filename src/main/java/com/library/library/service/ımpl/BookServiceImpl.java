package com.library.library.service.ımpl;

import com.library.library.dto.DtoBook;
import com.library.library.dto.DtoBookIU;
import com.library.library.dto.UpdateBookRequest;
import com.library.library.exception.BaseException;
import com.library.library.exception.MessageType;
import com.library.library.model.Author;
import com.library.library.model.Book;
import com.library.library.model.Category;
import com.library.library.model.Loan;
import com.library.library.repository.AuthorsRepository;
import com.library.library.repository.BookRepository;
import com.library.library.repository.CategoryRepository;
import com.library.library.repository.LoanRepository;
import com.library.library.service.BookService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorsRepository authorsRepository;
    private final CategoryRepository  categoryRepository;
    private final LoanRepository loanRepository;

    public BookServiceImpl(BookRepository bookRepository, AuthorsRepository authorsRepository, CategoryRepository categoryRepository, LoanRepository loanRepository) {
        this.bookRepository = bookRepository;
        this.authorsRepository = authorsRepository;
        this.categoryRepository = categoryRepository;
        this.loanRepository = loanRepository;
    }

    @Override
    public Book createBook(DtoBookIU dtoBookIU) {
        Set<Author> authors = new HashSet<>(authorsRepository.findAllById(dtoBookIU.getAuthorIds()));
        Set<Category> categories = new HashSet<>(categoryRepository.findAllById(dtoBookIU.getCategoryIds()));
        Book book = new Book();
        BeanUtils.copyProperties(dtoBookIU, book);
        book.setAuthors(authors);
        book.setCategories(categories);
        System.out.println(dtoBookIU.getTitle()  + " Adlı kitap eklendi.");
        return bookRepository.save(book);

    }


    @Override
    public void deleteBook(DtoBookIU dtoBookIU) {
        Optional<Book> OptBookId = bookRepository.findById(dtoBookIU.getId());
        if (OptBookId.isEmpty()){
            throw new BaseException(MessageType.INVALID_BOOK_NAME, HttpStatus.BAD_REQUEST);
        }
        Optional<Loan> OptLoanId = loanRepository.findById(dtoBookIU.getId());
        if (!OptLoanId.isEmpty()){
            throw new BaseException(MessageType.BOOK_ON_LOAN,HttpStatus.BAD_REQUEST);
        }
        bookRepository.deleteById(dtoBookIU.getId());
        System.out.println(dtoBookIU.getTitle()  + " Adlı kitap  silindi.");
    }

    @Override
    public DtoBook updateBook(Long bookId, UpdateBookRequest request) {
        Optional<Book> OptBookId = bookRepository.findById(bookId);
        if (OptBookId.isEmpty()){
            throw new BaseException(MessageType.INVALID_BOOK_NAME,HttpStatus.BAD_REQUEST);
        }
        Set<Author> authors = new HashSet<>(authorsRepository.findAllById(request.authorIds()));
        Set<Category> categories = new HashSet<>(categoryRepository.findAllById(request.categoryIds()));

        if (authors.isEmpty()){
            throw new BaseException(MessageType.AUTHOR_NOT_FOUND,HttpStatus.BAD_REQUEST);
        }
        if (categories.isEmpty()){
            throw new BaseException(MessageType.CATEGORY_NOT_FOUND,HttpStatus.BAD_REQUEST);
        }
        Book book = OptBookId.get();
        book.setTitle(request.title());
        book.setProductionYear(request.productionYear());
        book.setPageOfNumber(request.pageOfNumber());
        book.setAuthors(authors);
        book.setCategories(categories);
        book.setIsbn(request.isbn());
        Book savedBook = bookRepository.save(book);
        DtoBook dtoBook = new DtoBook();
        BeanUtils.copyProperties(savedBook,dtoBook);


        return  dtoBook;
    }
}
