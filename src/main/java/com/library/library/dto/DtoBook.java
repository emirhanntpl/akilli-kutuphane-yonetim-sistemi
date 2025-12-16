package com.library.library.dto;

import com.library.library.model.Author;
import com.library.library.model.Category;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtoBook extends DtoBaseEntity{

    @NotNull
    private String title;
    @NotNull
    private Set<Author> authors = new HashSet<>();
    @NotNull
    private Set<Category> categories = new HashSet<>();
    @NotNull
    private String isbn;
    @NotNull
    private int productionYear;
    @NotNull
    private int pageOfNumber;
    
    // YENİ EKLENEN ALAN
    private Integer stock; 

}
