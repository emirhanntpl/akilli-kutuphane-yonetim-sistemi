package com.library.library.service.impl;

import com.library.library.dto.DtoAuthor;
import com.library.library.dto.DtoAuthorIU;
import com.library.library.exception.BaseException;
import com.library.library.exception.MessageType;
import com.library.library.model.Author;
import com.library.library.repository.AuthorsRepository;
import com.library.library.service.AuthorService;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorsRepository authorsRepository;

    public AuthorServiceImpl(AuthorsRepository authorsRepository) {
        this.authorsRepository = authorsRepository;
    }
    @Override
    public DtoAuthor saveAuthor(DtoAuthorIU dtoAuthorIU) {
        Author author=new Author();
        author.setFirstName(dtoAuthorIU.getFirstName());
        author.setLastName(dtoAuthorIU.getLastName());
        Author savedAuthor=authorsRepository.save(author);
        DtoAuthor dtoAuthor=new DtoAuthor();
        BeanUtils.copyProperties(savedAuthor,dtoAuthor);
       return  dtoAuthor;
    }

    @Override
    public DtoAuthor updateAuthor(Long id, DtoAuthorIU dtoAuthorIU) {
        Author author = authorsRepository.findById(id).orElseThrow(() -> new BaseException(MessageType.AUTHOR_NOT_FOUND, HttpStatus.BAD_REQUEST));
        author.setFirstName(dtoAuthorIU.getFirstName());
        author.setLastName(dtoAuthorIU.getLastName());
        Author savedAuthor = authorsRepository.save(author);
        DtoAuthor dtoAuthor = new DtoAuthor();
        BeanUtils.copyProperties(savedAuthor, dtoAuthor);
        return dtoAuthor;
    }


    @Override
    public List<DtoAuthor> getAllAuthors() {
        List<Author> allAuthor = authorsRepository.findAll();
        List<DtoAuthor> dtoAuthorResponses = new ArrayList<>();
        for (Author author : allAuthor) {
            DtoAuthor dtoAuthor = new DtoAuthor();
            dtoAuthor.setId(author.getId());
            dtoAuthor.setFirstName(author.getFirstName());
            dtoAuthor.setLastName(author.getLastName());

            dtoAuthorResponses.add(dtoAuthor);
        }
        return dtoAuthorResponses;
    }


    @Override
    public void deleteAuthor(Long authorId) {
        if (authorId==null){
      throw new BaseException(MessageType.INVALID_AUTHOR_ID, HttpStatus.BAD_REQUEST);
        }
        else{
            authorsRepository.deleteById(authorId);
            System.out.println("Yazar silindi.");
        }






    }
}
