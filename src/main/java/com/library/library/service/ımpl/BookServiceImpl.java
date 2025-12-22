package com.library.library.service.ımpl;

import com.library.library.dto.DtoBook;
import com.library.library.dto.DtoBookIU;
import com.library.library.dto.UpdateBookRequest;
import com.library.library.exception.BaseException;
import com.library.library.exception.MessageType;
import com.library.library.model.*;
import com.library.library.repository.*;
import com.library.library.service.BookService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorsRepository authorsRepository;
    private final CategoryRepository  categoryRepository;
    private final LoanRepository loanRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository; // YENİ

    public BookServiceImpl(BookRepository bookRepository, AuthorsRepository authorsRepository, CategoryRepository categoryRepository, LoanRepository loanRepository, ReservationRepository reservationRepository, UserRepository userRepository, ReviewRepository reviewRepository) { // GÜNCELLENDİ
        this.bookRepository = bookRepository;
        this.authorsRepository = authorsRepository;
        this.categoryRepository = categoryRepository;
        this.loanRepository = loanRepository;
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository; // YENİ
    }

    @Override
    public List<DtoBook> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DtoBook> getBooksByCategoryId(Long categoryId) {
        return bookRepository.findBooksByCategoryId(categoryId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    @Override
    public List<DtoBook> getBooksByAuthorId(Long authorId) {
        return bookRepository.findBooksByAuthorId(authorId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private DtoBook convertToDto(Book book) {
        DtoBook dtoBook = new DtoBook();
        dtoBook.setId(book.getId());
        dtoBook.setTitle(book.getTitle());
        dtoBook.setAuthors(book.getAuthors());
        dtoBook.setCategories(book.getCategories());
        dtoBook.setIsbn(book.getIsbn());
        dtoBook.setProductionYear(book.getProductionYear());
        dtoBook.setPageOfNumber(book.getPageOfNumber());
        dtoBook.setStock(book.getStock());
        return dtoBook;
    }

    @Override
    @Transactional
    public Book createBook(DtoBookIU dtoBookIU) {
        Optional<Book> existingBookOpt = bookRepository.findByTitle(dtoBookIU.getTitle());

        if (existingBookOpt.isPresent()) {
            Book existingBook = existingBookOpt.get();
            existingBook.setStock(existingBook.getStock() + dtoBookIU.getStock());
            System.out.println("'" + existingBook.getTitle() + "' adlı kitabın stoğu " + dtoBookIU.getStock() + " kadar artırıldı.");
            return bookRepository.save(existingBook);
        } else {
            Set<Author> authors = new HashSet<>(authorsRepository.findAllById(dtoBookIU.getAuthorIds()));
            Set<Category> categories = new HashSet<>(categoryRepository.findAllById(dtoBookIU.getCategoryIds()));
            
            Book book = new Book();
            BeanUtils.copyProperties(dtoBookIU, book);
            book.setStock(dtoBookIU.getStock());
            
            book.setAuthors(authors);
            book.setCategories(categories);
            
            System.out.println("'" + dtoBookIU.getTitle()  + "' adlı yeni kitap eklendi.");
            return bookRepository.save(book);
        }
    }

    @Override
    @Transactional
    public void deleteBook(DtoBookIU dtoBookIU) {
        Book book = bookRepository.findById(dtoBookIU.getId())
                .orElseThrow(() -> new BaseException(MessageType.INVALID_BOOK_NAME, HttpStatus.BAD_REQUEST));

        // 1. Aktif ödünç kontrolü (Hala iade edilmemişse silme)
        boolean onLoan = loanRepository.existsByBookIdAndReturnDateIsNull(book.getId());
        if (onLoan){
            throw new BaseException(MessageType.BOOK_ON_LOAN,HttpStatus.BAD_REQUEST);
        }

        // 2. Aktif rezervasyon kontrolü (Bekleyen veya bildirilmişse silme)
        boolean isReserved = reservationRepository.existsByBookIdAndStatus(book.getId(), ReservationStatus.WAITING) ||
                             reservationRepository.existsByBookIdAndStatus(book.getId(), ReservationStatus.NOTIFIED);
        if (isReserved) {
            throw new BaseException(MessageType.BOOK_IS_RESERVED, HttpStatus.BAD_REQUEST);
        }

        // 3. Geçmiş ödünç kayıtlarını sil (İade edilmiş olanlar)
        List<Loan> pastLoans = loanRepository.findByBookId(book.getId());
        loanRepository.deleteAll(pastLoans);

        // 4. Geçmiş rezervasyon kayıtlarını sil (Tamamlanmış veya iptal edilmiş olanlar)
        List<Reservation> pastReservations = reservationRepository.findByBookId(book.getId());
        reservationRepository.deleteAll(pastReservations);

        // 5. Kitaba ait yorumları sil
        List<Review> reviews = reviewRepository.findByBookId(book.getId());
        reviewRepository.deleteAll(reviews);

        // 6. Favori ilişkilerini temizle
        List<User> usersWithFavorite = userRepository.findByFavoriteBooksContains(book);
        for (User user : usersWithFavorite) {
            user.getFavoriteBooks().remove(book);
        }
        userRepository.saveAll(usersWithFavorite);

        // 7. Son olarak kitabı sil
        bookRepository.delete(book);
        System.out.println(book.getTitle()  + " adlı kitap ve tüm ilişkili verileri silindi.");
    }

    @Override
    @Transactional
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
        book.setStock(request.stock());
        Book savedBook = bookRepository.save(book);
        
        return convertToDto(savedBook);
    }

    @Override
    public void updateAllStocks(Integer stock) {
        List<Book> books = bookRepository.findAll();
        for (Book book : books) {
            book.setStock(stock);
        }
        bookRepository.saveAll(books);
    }
}
