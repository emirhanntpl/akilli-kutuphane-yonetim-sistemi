package com.library.library.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record UpdateUserRequest(

        String name,
        String email,
        String address


) {
}
