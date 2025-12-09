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


    @PostMapping("api/admin/category")
    @Override
    public RootEntity<DtoCategory> saveCategory(@RequestBody @Valid DtoCategoryIU dtoCategoryIU) {
        return ok(categoryService.saveCategory(dtoCategoryIU));
    }

    @PutMapping("api/admin/categoryUpdate/{id}")
    @Override
    public RootEntity<DtoCategory> updateCategory( @RequestBody @Valid Long id, DtoCategoryIU dtoCategoryIU) {
        return ok(categoryService.updateCategory(id, dtoCategoryIU));
    }

    @GetMapping("api/categoryAll")
    @Override
    public RootEntity<List<DtoCategory>> getAllCategory() {
        return ok(categoryService.getAllCategory());
    }

    @DeleteMapping("/api/admin/categoryDelete/{id}")
    @Override
    public ResponseEntity<Void> deleteCategory(@RequestBody @Valid Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("api/categoryGet/{categoryId}")
    @Override
    public RootEntity<DtoCategory> getCategoryById(@RequestBody @Valid Long categoryId) {
        return ok(categoryService.getCategoryById(categoryId));
    }
}
