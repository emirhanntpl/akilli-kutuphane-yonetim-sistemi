package com.library.library.controller;

import com.library.library.dto.DtoCategory;
import com.library.library.dto.DtoCategoryIU;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryController {

    RootEntity<DtoCategory> saveCategory(DtoCategoryIU dtoCategoryIU);

    RootEntity<DtoCategory> updateCategory(Long id,DtoCategoryIU dtoCategoryIU);

    RootEntity<List<DtoCategory>> getAllCategory();

    ResponseEntity<Void> deleteCategory(Long id);

    RootEntity<DtoCategory> getCategoryById(Long categoryId);



}
