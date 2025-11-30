package com.library.library.service;

import com.library.library.dto.DtoUserUpdate;
import com.library.library.dto.UpdateUserRequest;
import com.library.library.exception.BaseException;
import com.library.library.exception.MessageType;
import com.library.library.model.CreateUserRequest;
import com.library.library.model.Loan;
import com.library.library.model.User;
import com.library.library.repository.LoanRepository;
import com.library.library.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final LoanRepository loanRepository;

    public Optional<User> getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User createUser(CreateUserRequest request) {
        User newUser = User.builder()
                .name(request.name())
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .role(request.authorities())
                .build();
        return userRepository.save(newUser);
    }

    public DtoUserUpdate updateUser(Long userId, UpdateUserRequest request) {
        Optional<User> OptUser = userRepository.findById(userId);
        if (OptUser.isEmpty()) {
            throw new BaseException(MessageType.NO_RECORD_EXIST, HttpStatus.NOT_FOUND);
        }
        User user = OptUser.get();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setAddress(request.address());

        User savedUser = userRepository.save(user);

        return new DtoUserUpdate(
                savedUser.getName(),
                savedUser.getAddress(),
                savedUser.getEmail());
    }

    public void deleteUser(Long userId) {
        Optional<User> OptUser = userRepository.findById(userId);
        if (OptUser.isEmpty()){
            throw new BaseException(MessageType.NO_RECORD_EXIST, HttpStatus.NOT_FOUND);
        }
        List<Loan> OptLoans = loanRepository.findByUserIdAndReturnDateIsNull(userId);
        if (!OptLoans.isEmpty()){
            throw new BaseException(MessageType.USER_HAS_UNRETURNED_BOOKS,HttpStatus.BAD_REQUEST);
        }
        userRepository.deleteById(userId);
        System.out.println( OptUser.get().getName()  +" adlı kullanıcı silindi.");
    }
}
