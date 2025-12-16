package com.library.library.service;

import com.library.library.dto.DtoUser;
import com.library.library.dto.UpdateUserRequest;
import com.library.library.exception.BaseException;
import com.library.library.exception.MessageType;
import com.library.library.model.CreateUserRequest;
import com.library.library.model.Loan;
import com.library.library.model.User;
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
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
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
        return dtoUser;
    }

    public Optional<User> getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // GÜNCELLENMİŞ METOT
    public User createUser(CreateUserRequest request) {
        User newUser = User.builder()
                .name(request.name())
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .email(request.email())     // EKLENDİ
                .address(request.address()) // EKLENDİ
                .role(request.authorities())
                .build();
        return userRepository.save(newUser);
    }

    public DtoUser updateUser(Long userId, UpdateUserRequest request) {
        Optional<User> OptUser = userRepository.findById(userId);
        if (OptUser.isEmpty()) {
            throw new BaseException(MessageType.NO_RECORD_EXIST, HttpStatus.NOT_FOUND);
        }
        User user = OptUser.get();
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
        Optional<User> OptUser = userRepository.findById(userId);
        if (OptUser.isEmpty()){
            throw new BaseException(MessageType.NO_RECORD_EXIST, HttpStatus.NOT_FOUND);
        }
        List<Loan> OptLoans = loanRepository.findByUserIdAndReturnDateIsNull(userId);
        if (!OptLoans.isEmpty()){
            throw new BaseException(MessageType.USER_HAS_UNRETURNED_BOOKS,HttpStatus.BAD_REQUEST);
        }
        refreshTokenRepository.deleteByUserId(userId);
        userRepository.deleteById(userId);
        System.out.println( OptUser.get().getName()  +" adlı kullanıcı silindi.");
    }
}
