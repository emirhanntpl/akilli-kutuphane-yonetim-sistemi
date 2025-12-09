package com.library.library.service;

import com.library.library.dto.DtoCategory;
import com.library.library.dto.DtoCategoryIU;

import java.util.List;

public interface CategoryService {

    public DtoCategory saveCategory(DtoCategoryIU dtoCategoryIU);
    public DtoCategory updateCategory(Long id,DtoCategoryIU dtoCategoryIU);
    public List<DtoCategory> getAllCategory();
    public void deleteCategory(Long categoryId);
    public DtoCategory getCategoryById(Long categoryId);



}
