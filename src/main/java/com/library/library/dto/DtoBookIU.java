package com.library.library.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    private Set<@Min(value = 1, message = "Yazar ID 0'dan büyük olmalıdır") Long> authorIds = new HashSet<>();
    
    @NotNull
    private Set<@Min(value = 1, message = "Kategori ID 0'dan büyük olmalıdır") Long> categoryIds = new HashSet<>();
    
    @NotNull
    @Pattern(regexp = "^(?!0$).*", message = "ISBN 0 olamaz")
    private String isbn;
    
    @NotNull
    @Min(value = 1, message = "Yayın yılı 0'dan büyük olmalıdır")
    private int productionYear;
    
    @NotNull
    @Min(value = 1, message = "Sayfa sayısı 0'dan büyük olmalıdır")
    private int pageOfNumber;
    
    @NotNull
    @Min(value = 1, message = "Stok 0'dan büyük olmalıdır")
    private Integer stock;
}
