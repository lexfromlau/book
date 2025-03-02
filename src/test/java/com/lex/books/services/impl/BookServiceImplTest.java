package com.lex.books.services.impl;

import com.lex.books.domain.Book;
import com.lex.books.domain.BookEntity;
import com.lex.books.repositories.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.lex.books.TestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {
    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl underTest;

    @Test
    public void testThatBookIsSaved() {
        Book book = testBook();
        BookEntity bookEntity = testBookEntity();
        when(bookRepository.save(bookEntity)).thenReturn(bookEntity);
        Book result = underTest.save(book);
        assertEquals(book, result);
    }

    @Test
    public void testThatFindByIdReturnsEmptyWhenNoBook() {
        when(bookRepository.findById(eq(ISBN))).thenReturn(Optional.empty());
        Optional<Book> result = underTest.findById(ISBN);
        assertEquals(Optional.empty(), result);
    }

    @Test
    public void testThatFindByIdReturnsBookWhenExists() {
        Book book = testBook();
        BookEntity bookEntity = testBookEntity();
        when(bookRepository.findById(eq(book.getIsbn()))).thenReturn(Optional.of(bookEntity));
        Optional<Book> result = underTest.findById(book.getIsbn());
        assertEquals(Optional.of(book), result);
    }

    @Test
    public void testListBooksReturnsEmptyListWhenNoBooksExists() {
        when(bookRepository.findAll()).thenReturn(new ArrayList<BookEntity>());
        List<Book> result = underTest.getAll();
        assertEquals(0, result.size());
    }

    @Test
    public void testListBooksReturnsBooksWhenExists() {
        BookEntity bookEntity = testBookEntity();
        when(bookRepository.findAll()).thenReturn(List.of(bookEntity));
        List<Book> result = underTest.getAll();
        assertEquals(1, result.size());
    }

    @Test
    public void testIsBookExistsReturnsFalseWhenBookDoesntExists() {
        when(bookRepository.existsById(any())).thenReturn(false);
        boolean result = underTest.isBookExists(testBook());
        assertFalse(result);
    }

    @Test
    public void testIsBookExistsReturnsTrueWhenBookDoesExists() {
        Book book = testBook();
        when(bookRepository.existsById(book.getIsbn())).thenReturn(true);
        boolean result = underTest.isBookExists(testBook());
        assertTrue(result);
    }

    @Test
    public void testDeleteBookDeleteBook() {
        underTest.deleteBookById(ISBN);
        verify(bookRepository, times(1)).deleteById(eq(ISBN));
    }
}
