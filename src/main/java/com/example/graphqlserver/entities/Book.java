package com.example.graphqlserver.entities;

import java.util.Arrays;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Book {
    private String id;
    private String name;
    private int pageCount;
    private String authorId;
    private Author author;

    private static List<Book> books = Arrays.asList(
            // Book 1 is preloaded with Author to demonstrate
            Book.builder().id("book-1").name("Effective Java").pageCount(416).authorId("author-1").author(Author.getById("author-1")).build(),
            Book.builder().id("book-2").name("Hitchhiker's Guide to the Galaxy").pageCount(208).authorId("author-2").build(),
            Book.builder().id("book-3").name("Down Under").pageCount(436).authorId("author-3").build());

    public static Book getById(String id) {
        return books.stream()
                .filter(book -> book.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public static List<Book> getAll() {
        return books;
    }
}
