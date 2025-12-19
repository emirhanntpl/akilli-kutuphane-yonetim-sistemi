package com.library.library.service;

import com.library.library.dto.DtoBook;
import com.library.library.dto.DtoTransaction;
import com.library.library.dto.DtoUser;
import com.library.library.dto.LoadBalanceRequest;
import com.library.library.dto.UpdateUserRequest;
import com.library.library.exception.BaseException;
import com.library.library.exception.MessageType;
import com.library.library.model.*;
import com.library.library.repository.BookRepository;
import com.library.library.repository.LoanRepository;
import com.library.library.repository.RefreshTokenRepository;
import com.library.library.repository.TransactionRepository;
import com.library.library.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final LoanRepository loanRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BookRepository bookRepository;
    private final EmailService emailService;
    private final TransactionRepository transactionRepository; // EKLENDİ

    public List<DtoUser> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private DtoUser convertToDto(User user) {
        DtoUser dtoUser = new DtoUser();
        dtoUser.setId(user.getId());
        dtoUser.setName(user.getName());
        dtoUser.setUsername(user.getUsername());
        dtoUser.setEmail(user.getEmail());
        dtoUser.setAddress(user.getAddress());
        dtoUser.setRoles(user.getRole());
        dtoUser.setPenalty((int) user.getPenalty());
        dtoUser.setBalance(user.getBalance());
        return dtoUser;
    }

    public Optional<User> getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public User createUser(CreateUserRequest request) {
        User newUser = User.builder()
                .name(request.name())
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .email(request.email())
                .address(request.address())
                .role(request.authorities())
                .balance(0.0)
                .build();
        
        User savedUser = userRepository.save(newUser);
        
        System.out.println("Kullanıcı oluşturuldu. Mail gönderimi deneniyor...");
        String subject = "Kütüphanemize Hoş Geldiniz!";
        String text = "Merhaba " + savedUser.getName() + ",\n\nKütüphane sistemimize kaydınız başarıyla tamamlanmıştır.";
        emailService.sendEmail(savedUser.getEmail(), subject, text);

        return savedUser;
    }

    public DtoUser updateUser(Long userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(MessageType.USERNAME_NOT_FOUND, HttpStatus.NOT_FOUND));
        user.setName(request.name());
        user.setEmail(request.email());
        user.setAddress(request.address());
        if (request.roles() != null && !request.roles().isEmpty()) {
            user.setRole(request.roles());
        }
        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(MessageType.NO_RECORD_EXIST, HttpStatus.NOT_FOUND));

        List<Loan> activeLoans = loanRepository.findByUserIdAndReturnDateIsNull(userId);
        if (!activeLoans.isEmpty()){
            throw new BaseException(MessageType.USER_HAS_UNRETURNED_BOOKS, HttpStatus.BAD_REQUEST);
        }

        user.getFavoriteBooks().clear();
        user.getRole().clear();
        
        refreshTokenRepository.deleteByUserId(userId);

        userRepository.delete(user);
    }

    public Double getPenalty(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(MessageType.USERNAME_NOT_FOUND, HttpStatus.NOT_FOUND));
        return user.getPenalty();
    }

    // --- FAVORİ İŞLEMLERİ ---

    @Transactional
    public void addFavorite(Long userId, Long bookId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(MessageType.USERNAME_NOT_FOUND, HttpStatus.NOT_FOUND));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BaseException(MessageType.INVALID_BOOK_NAME, HttpStatus.NOT_FOUND));
        user.getFavoriteBooks().add(book);
        userRepository.save(user);
    }

    @Transactional
    public void removeFavorite(Long userId, Long bookId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(MessageType.USERNAME_NOT_FOUND, HttpStatus.NOT_FOUND));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BaseException(MessageType.INVALID_BOOK_NAME, HttpStatus.NOT_FOUND));
        user.getFavoriteBooks().remove(book);
        userRepository.save(user);
    }

    public List<DtoBook> getFavorites(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(MessageType.USERNAME_NOT_FOUND, HttpStatus.NOT_FOUND));
        
        return user.getFavoriteBooks().stream().map(book -> {
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
        }).collect(Collectors.toList());
    }

    // --- BAKİYE VE BORÇ İŞLEMLERİ ---

    @Transactional
    public Double loadBalance(Long userId, LoadBalanceRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(MessageType.USERNAME_NOT_FOUND, HttpStatus.NOT_FOUND));
        
        System.out.println("Kart Bilgileri Doğrulandı: " + request.cardNumber());
        
        double currentBalance = user.getBalance() != null ? user.getBalance() : 0.0;
        user.setBalance(currentBalance + request.amount());
        
        // İşlem Kaydı
        Transaction transaction = Transaction.builder()
                .user(user)
                .amount(request.amount())
                .description("Bakiye Yükleme")
                .transactionDate(LocalDateTime.now())
                .type(TransactionType.INCOME)
                .build();
        transactionRepository.save(transaction);
        
        return userRepository.save(user).getBalance();
    }

    @Transactional
    public void payPenalty(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(MessageType.USERNAME_NOT_FOUND, HttpStatus.NOT_FOUND));
        
        double penalty = user.getPenalty();
        double balance = user.getBalance() != null ? user.getBalance() : 0.0;

        if (penalty <= 0) {
            throw new RuntimeException("Ödenecek borç bulunmamaktadır.");
        }

        if (balance < penalty) {
            throw new RuntimeException("Yetersiz bakiye. Lütfen bakiye yükleyin.");
        }

        user.setBalance(balance - penalty);
        user.setPenalty(0.0);
        
        // İşlem Kaydı (Borç ödemesi de kasaya gelir olarak girer)
        Transaction transaction = Transaction.builder()
                .user(user)
                .amount(penalty)
                .description("Borç Ödeme")
                .transactionDate(LocalDateTime.now())
                .type(TransactionType.INCOME)
                .build();
        transactionRepository.save(transaction);

        userRepository.save(user);
    }
    
    public Double getBalance(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(MessageType.USERNAME_NOT_FOUND, HttpStatus.NOT_FOUND));
        return user.getBalance() != null ? user.getBalance() : 0.0;
    }

    // --- ADMİN İŞLEMLERİ ---

    @Transactional
    public void addPenaltyToUser(Long userId, Double amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(MessageType.USERNAME_NOT_FOUND, HttpStatus.NOT_FOUND));
        user.addPenalty(amount);
        userRepository.save(user);
    }

    @Transactional
    public void removePenaltyFromUser(Long userId, Double amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(MessageType.USERNAME_NOT_FOUND, HttpStatus.NOT_FOUND));
        double currentPenalty = user.getPenalty();
        if (currentPenalty < amount) {
            user.setPenalty(0.0);
        } else {
            user.setPenalty(currentPenalty - amount);
        }
        userRepository.save(user);
    }

    public List<DtoTransaction> getAllTransactions() {
        return transactionRepository.findAllByOrderByTransactionDateDesc().stream()
                .map(t -> new DtoTransaction(
                        t.getId(),
                        t.getUser().getUsername(),
                        t.getAmount(),
                        t.getDescription(),
                        t.getTransactionDate(),
                        t.getType()
                ))
                .collect(Collectors.toList());
    }
}
