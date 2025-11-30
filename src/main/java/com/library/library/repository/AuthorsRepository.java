package com.library.library.repository;

import com.library.library.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorsRepository  extends JpaRepository<Author, Long> {

    public Optional<Author> findById(Long id);



}
