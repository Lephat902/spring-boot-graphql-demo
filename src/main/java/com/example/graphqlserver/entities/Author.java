package com.example.graphqlserver.entities;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Author {
    private String id;
    private String firstName;
    private String lastName;

    private static List<Author> authors = Arrays.asList(
            Author.builder().id("author-1").firstName("Joshua").lastName("Bloch").build(),
            Author.builder().id("author-2").firstName("Douglas").lastName("Adams").build(),
            Author.builder().id("author-3").firstName("Bill").lastName("Bryson").build());

    public static Author getById(String id) {
        return authors.stream()
                .filter(author -> author.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public static List<Author> getByIds(Collection<String> ids) {
        return authors.stream()
                .filter(author -> ids.contains(author.getId()))
                .toList();
    }
}
