package com.library.library.controller;

import com.library.library.dto.DtoUserUpdate;
import com.library.library.dto.UpdateUserRequest;
import com.library.library.model.CreateUserRequest;
import com.library.library.model.User;
import org.springframework.http.ResponseEntity;

public interface UserController {


    RootEntity<User> createUser(CreateUserRequest request);

    RootEntity<DtoUserUpdate> updateUser(Long userId, UpdateUserRequest request);

    ResponseEntity<Void> deleteUser(Long userId);
}
