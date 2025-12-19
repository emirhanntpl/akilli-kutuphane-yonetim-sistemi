package com.library.library.controller.Impl;

import com.library.library.controller.RestBaseController;
import com.library.library.controller.RootEntity;
import com.library.library.dto.DtoBook;
import com.library.library.dto.DtoTransaction;
import com.library.library.dto.DtoUser;
import com.library.library.dto.LoadBalanceRequest;
import com.library.library.dto.UpdateUserRequest;
import com.library.library.model.CreateUserRequest;
import com.library.library.service.UserService;
import jakarta.validation.Valid;
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

    @GetMapping("/{id}/penalty")
    public RootEntity<Double> getPenalty(@PathVariable Long id) {
        return ok(userService.getPenalty(id));
    }

    @PostMapping
    public RootEntity<String> createUser(@RequestBody @Valid CreateUserRequest request) {
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

    // --- FAVORİ ENDPOINT'LERİ ---

    @GetMapping("/{userId}/favorites")
    public RootEntity<List<DtoBook>> getFavorites(@PathVariable Long userId) {
        return ok(userService.getFavorites(userId));
    }

    @PostMapping("/{userId}/favorites/{bookId}")
    public RootEntity<String> addFavorite(@PathVariable Long userId, @PathVariable Long bookId) {
        userService.addFavorite(userId, bookId);
        return ok("Kitap favorilere eklendi.");
    }

    @DeleteMapping("/{userId}/favorites/{bookId}")
    public RootEntity<String> removeFavorite(@PathVariable Long userId, @PathVariable Long bookId) {
        userService.removeFavorite(userId, bookId);
        return ok("Kitap favorilerden çıkarıldı.");
    }

    // --- BAKİYE VE BORÇ ENDPOINT'LERİ ---

    @PostMapping("/{userId}/load-balance")
    public RootEntity<Double> loadBalance(@PathVariable Long userId, @RequestBody @Valid LoadBalanceRequest request) {
        return ok(userService.loadBalance(userId, request));
    }

    @PostMapping("/{userId}/pay-penalty")
    public RootEntity<String> payPenalty(@PathVariable Long userId) {
        userService.payPenalty(userId);
        return ok("Borç başarıyla ödendi.");
    }

    @GetMapping("/{userId}/balance")
    public RootEntity<Double> getBalance(@PathVariable Long userId) {
        return ok(userService.getBalance(userId));
    }

    // --- ADMİN ENDPOINTLERİ ---

    @PostMapping("/{userId}/penalty/add")
    public RootEntity<String> addPenalty(@PathVariable Long userId, @RequestParam Double amount) {
        userService.addPenaltyToUser(userId, amount);
        return ok("Borç eklendi.");
    }

    @PostMapping("/{userId}/penalty/remove")
    public RootEntity<String> removePenalty(@PathVariable Long userId, @RequestParam Double amount) {
        userService.removePenaltyFromUser(userId, amount);
        return ok("Borç silindi.");
    }

    @GetMapping("/transactions")
    public RootEntity<List<DtoTransaction>> getAllTransactions() {
        return ok(userService.getAllTransactions());
    }
}
