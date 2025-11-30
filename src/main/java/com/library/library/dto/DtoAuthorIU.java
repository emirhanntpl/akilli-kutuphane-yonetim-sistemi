package com.library.library.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtoAuthorIU extends  DtoBaseEntity {

    @NotNull
    private String firstName;
    @NotNull
    private String lastName;


}
