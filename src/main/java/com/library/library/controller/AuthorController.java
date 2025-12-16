package com.library.library.controller;

import com.library.library.dto.DtoAuthor;
import com.library.library.dto.DtoAuthorIU;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface AuthorController {
    RootEntity<DtoAuthor> saveAuthor(@RequestBody DtoAuthorIU dtoAuthorIU);
    RootEntity<DtoAuthor> updateAuthor(@PathVariable("id") Long id, @RequestBody DtoAuthorIU dtoAuthorIU);
    RootEntity<List<DtoAuthor>> getAllAuthors(); // Düzeltildi
    ResponseEntity<Void> deleteAuthor(@PathVariable("id") Long authorId);
}
