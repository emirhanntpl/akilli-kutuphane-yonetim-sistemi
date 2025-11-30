package com.library.library.controller.Impl;

import com.library.library.controller.RestBaseController;
import com.library.library.controller.RootEntity;
import com.library.library.controller.UserController;
import com.library.library.dto.DtoUserUpdate;
import com.library.library.dto.UpdateUserRequest;
import com.library.library.model.CreateUserRequest;
import com.library.library.model.User;
import com.library.library.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/users")
@RestController
public class UserControllerImpl extends RestBaseController implements UserController {

    private final UserService userService;

    public UserControllerImpl(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    @Override
    public RootEntity<User> createUser(@RequestBody @Valid   CreateUserRequest request) {
        return ok(userService.createUser(request));
    }

    @PutMapping("/update")
    @Override
    public RootEntity<DtoUserUpdate> updateUser(@RequestBody @Valid  Long userId, @RequestBody @Valid  UpdateUserRequest request) {
        return ok(userService.updateUser(userId, request));
    }

    @DeleteMapping("/delete")
    @Override
    public ResponseEntity<Void> deleteUser(@RequestBody @Valid Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
