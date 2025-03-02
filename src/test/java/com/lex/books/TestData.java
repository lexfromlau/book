package com.lex.books;

import com.lex.books.domain.Book;
import com.lex.books.domain.BookEntity;

public class TestData {
    public static final String ISBN = "9876543210";

    private TestData() {

    }

    public static Book testBook() {
        return Book.builder()
                .isbn("0123456789")
                .author("Barbara Schaefer")
                .title("Lesereise Neapel")
                .build();
    }

    public static BookEntity testBookEntity() {
        return BookEntity.builder()
                .isbn("0123456789")
                .author("Barbara Schaefer")
                .title("Lesereise Neapel")
                .build();
    }
}
