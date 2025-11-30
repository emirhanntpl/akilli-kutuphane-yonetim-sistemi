package com.library.library.repository;

import com.library.library.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository  extends JpaRepository<Category,Long> {

    public Optional<Category> findById(Long id);


}
