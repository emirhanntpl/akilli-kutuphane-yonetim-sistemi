package com.library.library.model;

import java.util.Set;
public record CreateUserRequest(
        String name,
        String username,
        String password,
        Set<Role> authorities
) {
}
