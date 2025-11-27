package com.library.library.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "authors")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Author extends BaseEntity {

    @NotNull
    private String firstName;
    @NotNull
    private String lastName;

    @ManyToMany(mappedBy = "authors", fetch = FetchType.LAZY) // "author" -> "authors" olarak düzeltildi.
    private Set<Book> books = new HashSet<>();
}
