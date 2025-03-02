package com.lex.books.services;

import com.lex.books.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    Book save(Book book);

    Optional<Book> findById(String isbn);

    List<Book> getAll();

    boolean isBookExists(Book book);

    boolean deleteBookById(String isbn);
}
