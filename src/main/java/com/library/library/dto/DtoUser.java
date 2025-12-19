package com.library.library.dto;

import com.library.library.model.Role;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DtoUser extends DtoBaseEntity{
    
    @NotNull
    private String name;
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String email;
    @NotNull
    private String address;
    private int penalty;
    private Double balance; // YENİ EKLENDİ

    private Set<Role> roles;
}
