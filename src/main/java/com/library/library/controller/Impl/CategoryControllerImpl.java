package com.library.library.controller.Impl;

import com.library.library.controller.CategoryController;
import com.library.library.controller.RestBaseController;
import com.library.library.controller.RootEntity;
import com.library.library.dto.DtoCategory;
import com.library.library.dto.DtoCategoryIU;
import com.library.library.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
public class CategoryControllerImpl extends RestBaseController implements CategoryController {


    private final CategoryService categoryService;

    public CategoryControllerImpl(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @PostMapping("/api/admin/category")//çalışıyor
    @Override
    public RootEntity<DtoCategory> saveCategory(@RequestBody @Valid DtoCategoryIU dtoCategoryIU) {
        return ok(categoryService.saveCategory(dtoCategoryIU));
    }

    @PutMapping("/api/admin/category/{id}")//çalışıyor
    @Override
    public RootEntity<DtoCategory> updateCategory(@PathVariable("id") Long id, @RequestBody @Valid DtoCategoryIU dtoCategoryIU) {
        return ok(categoryService.updateCategory(id, dtoCategoryIU));
    }

    @GetMapping("/api/categoryAll")//çalışıyor
    @Override
    public RootEntity<List<DtoCategory>> getAllCategory() {
        return ok(categoryService.getAllCategory());
    }

    @DeleteMapping("/api/admin/category/delete/{id}")
    @Override
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") Long id) {//çalışıyor.
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/api/categoryGet/{categoryId}")//çalışıyor.
    @Override
    public RootEntity<DtoCategory> getCategoryById(@PathVariable("categoryId") Long categoryId) {
        return ok(categoryService.getCategoryById(categoryId));
    }
}
