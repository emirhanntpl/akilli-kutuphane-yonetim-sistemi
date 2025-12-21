package com.library.library.controller;

import com.library.library.dto.DtoBook;
import com.library.library.dto.DtoTransaction;
import com.library.library.dto.DtoUserUpdate;
import com.library.library.dto.LoadBalanceRequest;
import com.library.library.dto.UpdateUserRequest;
import com.library.library.model.CreateUserRequest;
import com.library.library.model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserController {


    RootEntity<User> createUser(CreateUserRequest request);

    RootEntity<DtoUserUpdate> updateUser(Long userId, UpdateUserRequest request);

    ResponseEntity<Void> deleteUser(Long userId);

    RootEntity<Double> getPenalty(Long userId);
    RootEntity<List<DtoBook>> getFavorites(Long userId);
    RootEntity<String> addFavorite(Long userId, Long bookId);
    RootEntity<String> removeFavorite(Long userId, Long bookId);
    
    RootEntity<Double> loadBalance(Long userId, LoadBalanceRequest request);
    RootEntity<String> payPenalty(Long userId);
    RootEntity<Double> getBalance(Long userId);

    RootEntity<String> addPenalty(Long userId, Double amount);
    RootEntity<String> removePenalty(Long userId, Double amount);
    
    // YENİ EKLENDİ
    RootEntity<List<DtoTransaction>> getAllTransactions();
}
