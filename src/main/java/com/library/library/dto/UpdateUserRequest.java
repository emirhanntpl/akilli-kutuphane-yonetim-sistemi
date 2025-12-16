package com.library.library.dto;

import com.library.library.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record UpdateUserRequest(
        @NotBlank
        String name,

        @Email
        String email,

        @NotBlank
        String address,

        Set<Role> roles
) {
}
