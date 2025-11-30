package com.library.library.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class DtoUserIU extends DtoBaseEntity{

    @NotNull
    private String name;
    @NotNull
    private String username;
    @NotNull
    private String password;
    private String email;
    private  String address;

}
