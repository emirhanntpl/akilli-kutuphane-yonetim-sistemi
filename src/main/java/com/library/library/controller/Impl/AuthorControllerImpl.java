package com.library.library.controller.Impl;

import com.library.library.controller.AuthorController;
import com.library.library.controller.RestBaseController;
import com.library.library.controller.RootEntity;
import com.library.library.dto.DtoAuthor;
import com.library.library.dto.DtoAuthorIU;
import com.library.library.service.AuthorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AuthorControllerImpl  extends RestBaseController implements AuthorController {

    private final AuthorService authorService;

    public AuthorControllerImpl(AuthorService authorService) {
        this.authorService = authorService;

    }

    @PostMapping("/api/author")
    @Override
    public RootEntity<DtoAuthor> saveAuthor(@RequestBody @Valid DtoAuthorIU dtoAuthorIU) {
        return ok(authorService.saveAuthor(dtoAuthorIU));
    }

    @PutMapping("/api/admin/author/{id}")
    @Override
    public RootEntity<DtoAuthor> updateAuthor(@PathVariable("id") Long id, @RequestBody @Valid DtoAuthorIU dtoAuthorIU) {
        return ok(authorService.updateAuthor(id, dtoAuthorIU));
    }

    @GetMapping("/api/author")
    @Override
    public List<DtoAuthor> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    @DeleteMapping("/api/admin/author/{id}")
    @Override
    public ResponseEntity<Void> deleteAuthor(@PathVariable("id") Long authorId) {
      authorService.deleteAuthor(authorId);
        return ResponseEntity.noContent().build();
    }
}