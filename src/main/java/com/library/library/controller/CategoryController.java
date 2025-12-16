package com.library.library.controller;

import com.library.library.dto.DtoCategory;
import com.library.library.dto.DtoCategoryIU;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface CategoryController {
    RootEntity<DtoCategory> saveCategory(@RequestBody DtoCategoryIU dtoCategoryIU);
    RootEntity<DtoCategory> updateCategory(@PathVariable("id") Long id, @RequestBody DtoCategoryIU dtoCategoryIU);
    RootEntity<List<DtoCategory>> getAllCategory();
    ResponseEntity<Void> deleteCategory(@PathVariable("id") Long id);
    RootEntity<DtoCategory> getCategoryById(@PathVariable("categoryId") Long categoryId);
}