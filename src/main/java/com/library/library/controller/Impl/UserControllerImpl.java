package com.library.library.controller.Impl;

import com.library.library.controller.RestBaseController;
import com.library.library.controller.RootEntity;
import com.library.library.dto.DtoUser;
import com.library.library.dto.UpdateUserRequest;
import com.library.library.model.CreateUserRequest;
import com.library.library.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/api/users")
public class UserControllerImpl extends RestBaseController {

    private final UserService userService;

    public UserControllerImpl(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public RootEntity<List<DtoUser>> getAllUsers() {
        return ok(userService.getAllUsers());
    }

    @PostMapping
    public RootEntity<DtoUser> createUser(@RequestBody @Valid CreateUserRequest request) {

        userService.createUser(request);
        return ok("Kullanıcı başarıyla oluşturuldu.");
    }

    @PutMapping("/{id}")
    public RootEntity<DtoUser> updateUser(@PathVariable Long id, @RequestBody @Valid UpdateUserRequest request) {
        return ok(userService.updateUser(id, request));
    }

    @DeleteMapping("/{id}")
    public RootEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ok("Kullanıcı başarıyla silindi.");
    }
}
