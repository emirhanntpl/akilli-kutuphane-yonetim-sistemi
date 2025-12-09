package com.library.library.controller;

import com.library.library.dto.DtoAuthor;
import com.library.library.dto.DtoAuthorIU;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AuthorController {

    public RootEntity<DtoAuthor> saveAuthor(DtoAuthorIU dtoAuthorIU);
    public RootEntity<DtoAuthor> updateAuthor(Long id, DtoAuthorIU dtoAuthorIU);
    public List<DtoAuthor> getAllAuthors();
    ResponseEntity<Void> deleteAuthor(Long authorId);

    }




