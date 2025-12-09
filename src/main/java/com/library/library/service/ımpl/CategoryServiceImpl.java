package com.library.library.service.ımpl;

import com.library.library.dto.DtoCategory;
import com.library.library.dto.DtoCategoryIU;
import com.library.library.exception.BaseException;
import com.library.library.exception.MessageType;
import com.library.library.model.Category;
import com.library.library.repository.CategoryRepository;
import com.library.library.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class CategoryServiceImpl implements CategoryService {

       private final  CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;

    }

    @Override
    public DtoCategory saveCategory(DtoCategoryIU dtoCategoryIU) {
        Category category=new Category();
        category.setCategory(dtoCategoryIU.getCategory());
        category.setCreateTime(new Date());
        Category savedCategory = categoryRepository.save(category);
        DtoCategory dtoCategory=new DtoCategory();
        BeanUtils.copyProperties(savedCategory, dtoCategory);
        return dtoCategory;
    }

    @Override
    public DtoCategory updateCategory(Long id, DtoCategoryIU dtoCategoryIU) {
        Category category=categoryRepository.findById(id).orElseThrow(() -> new BaseException(MessageType.CATEGORY_NOT_FOUND, HttpStatus.BAD_REQUEST));
        category.setCategory(dtoCategoryIU.getCategory());
        Category savedCategory=categoryRepository.save(category);
        DtoCategory dtoCategory=new DtoCategory();
        BeanUtils.copyProperties(savedCategory,dtoCategory);
        return dtoCategory;
    }


    @Override
    public List<DtoCategory> getAllCategory() {
        List<Category> allCategory = categoryRepository.findAll();
        List<DtoCategory> dtoCategoryResponses = new ArrayList<>();
        for (Category category : allCategory) {
            DtoCategory dtoCategory = new DtoCategory();
            dtoCategory.setId(category.getId());
            dtoCategory.setCategory(category.getCategory());
            dtoCategoryResponses.add(dtoCategory);
        }
        return  dtoCategoryResponses;
    }

    @Override
    public void deleteCategory(Long categoryId) {
        if (categoryId==null){
            throw new BaseException(MessageType.INVALID_CATEGORY_ID, HttpStatus.BAD_REQUEST);
        }
        else{
            categoryRepository.deleteById(categoryId);
            System.out.println("Kategori silindi.");
        }
    }

    @Override
    public DtoCategory getCategoryById(Long categoryId) {
        categoryRepository.findById(categoryId).orElseThrow(() -> new BaseException(MessageType.CATEGORY_NOT_FOUND, HttpStatus.BAD_REQUEST));
        Category category = categoryRepository.findById(categoryId).get();
        DtoCategory dtoCategory = new DtoCategory();
        BeanUtils.copyProperties(category, dtoCategory);
        return dtoCategory;
    }
}
