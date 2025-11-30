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
public class DtoBookIU extends DtoBaseEntity{

    @NotNull
    private String title;
    @NotNull
    private  Set<Long> authorIds = new HashSet<>();
    @NotNull
    private Set<Long> categoryIds = new HashSet<>();
    @NotNull
    private String isbn;
    @NotNull
    private int productionYear;
    @NotNull
    private int pageOfNumber;
}
