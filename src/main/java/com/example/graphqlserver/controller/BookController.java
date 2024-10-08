package com.example.graphqlserver.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.dataloader.DataLoader;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.graphql.execution.BatchLoaderRegistry;
import org.springframework.stereotype.Controller;

import com.example.graphqlserver.entities.Author;
import com.example.graphqlserver.entities.Book;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Controller
@Slf4j
public class BookController {
    public BookController(BatchLoaderRegistry registry) {
        registry.forTypePair(String.class, Author.class).registerMappedBatchLoader((authorIds, env) -> {
            var authors = Author.getByIds(authorIds);
            var authorMap = authors.stream()
                    .collect(Collectors.toMap(Author::getId, author -> author));
            return Mono.just(authorMap);
        });
    }

    @QueryMapping
    public List<Book> books() {
        return Book.getAll();
    }

    @QueryMapping
    public Book bookById(@Argument String id) {
        return Book.getById(id);
    }

    @SchemaMapping
    public CompletableFuture<Author> author(Book book, DataLoader<String, Author> loader) {
        if (book.getAuthor() != null)
            return CompletableFuture.completedFuture(book.getAuthor());
        log.info("USE LOADER: " + book.getAuthorId());
        return loader.load(book.getAuthorId());
    }
}
