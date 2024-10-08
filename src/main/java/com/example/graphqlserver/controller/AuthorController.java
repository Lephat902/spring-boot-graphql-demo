package com.example.graphqlserver.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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
public class AuthorController {
    @SuppressWarnings("unchecked")
    public AuthorController(BatchLoaderRegistry registry) {
        registry.forTypePair(String.class, (Class<List<Book>>) (Class<?>) List.class)
                .registerMappedBatchLoader((authorIds, env) -> {
                    log.info("USE DATA LOADER FOR AUTHORS: " + authorIds);
                    var authorToBooksMap = Book.getByAuthorIds(authorIds);
                    return Mono.just(authorToBooksMap);
                });
    }

    @QueryMapping
    public List<Author> authors(@Argument List<String> ids) {
        return Author.getByIds(ids);
    }

    @QueryMapping
    public Author authorById(@Argument String id) {
        return Author.getById(id);
    }

    @SchemaMapping
    public CompletableFuture<List<Book>> books(Author author, DataLoader<String, List<Book>> loader) {
        if (author.getBooks() != null)
            return CompletableFuture.completedFuture(author.getBooks());
        return loader.load(author.getId());
    }
}
