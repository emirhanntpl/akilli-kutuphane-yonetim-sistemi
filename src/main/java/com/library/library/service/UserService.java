package com.library.library.service;

import com.library.library.dto.DtoBook;
import com.library.library.dto.DtoUser;
import com.library.library.dto.UpdateUserRequest;
import com.library.library.exception.BaseException;
import com.library.library.exception.MessageType;
import com.library.library.model.*;
import com.library.library.repository.BookRepository;
import com.library.library.repository.LoanRepository;
import com.library.library.repository.RefreshTokenRepository;
import com.library.library.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final LoanRepository loanRepository;
    private final RefreshTokenRepository refreshTokenRepository;

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
        return dtoUser;
    }

    public Optional<User> getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User createUser(CreateUserRequest request) {
        User newUser = User.builder()
                .name(request.name())
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .email(request.email())
                .address(request.address())
                .role(request.authorities())
                .build();
        return userRepository.save(newUser);
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
        if (!userRepository.existsById(userId)) {
            throw new BaseException(MessageType.NO_RECORD_EXIST, HttpStatus.NOT_FOUND);
        }
        List<Loan> activeLoans = loanRepository.findByUserIdAndReturnDateIsNull(userId);
        if (!activeLoans.isEmpty()){
            throw new BaseException(MessageType.USER_HAS_UNRETURNED_BOOKS, HttpStatus.BAD_REQUEST);
        }
        userRepository.deleteById(userId);
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

    public Set<Book> getFavorites(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(MessageType.USERNAME_NOT_FOUND, HttpStatus.NOT_FOUND));
        return user.getFavoriteBooks();
    }
}
