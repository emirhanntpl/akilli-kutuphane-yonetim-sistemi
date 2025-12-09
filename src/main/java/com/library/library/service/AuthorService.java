package com.library.library.service;

import com.library.library.dto.DtoAuthor;
import com.library.library.dto.DtoAuthorIU;

import java.util.List;

public interface AuthorService  {

    DtoAuthor saveAuthor(DtoAuthorIU dtoAuthorIU);

    DtoAuthor updateAuthor(Long id, DtoAuthorIU dtoAuthorIU);

    List<DtoAuthor> getAllAuthors();

    void deleteAuthor(Long authorId);



}
